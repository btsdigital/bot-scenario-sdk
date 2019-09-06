package kz.btsd.bot.botscenariosdk

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kz.btsd.bot.botscenariosdk.keyboards.InlineUniqueKeyboard
import kz.btsd.bot.botscenariosdk.scenario.ScenarioFactory
import kz.btsd.bot.botscenariosdk.session.Session
import kz.btsd.bot.botscenariosdk.session.SessionService
import kz.btsd.bot.botscenariosdk.session.SessionStored
import kz.btsd.bot.botscenariosdk.utils.IdProvider
import kz.btsd.bot.botscenariosdk.utils.LabelProvider
import kz.btsd.bot.botscenariosdk.utils.SelectionMatcher
import kz.btsd.bot.botscenariosdk.utils.builder.buildSendMessage
import kz.btsd.bot.botscenariosdk.utils.builder.buildSendUiState
import kz.btsd.bot.botscenariosdk.utils.enumInlineUniqueKeyboard
import kz.btsd.bot.botscenariosdk.utils.messengerId
import kz.btsd.bot.botscenariosdk.utils.parseEnum
import kz.btsd.bot.botsdk.LongPollingBot
import kz.btsd.messenger.bot.api.model.command.Command
import kz.btsd.messenger.bot.api.model.command.FormMessage
import kz.btsd.messenger.bot.api.model.command.InlineCommand
import kz.btsd.messenger.bot.api.model.command.InterfaceView
import kz.btsd.messenger.bot.api.model.command.QuickButtonCommand
import kz.btsd.messenger.bot.api.model.command.ReplyCommand
import kz.btsd.messenger.bot.api.model.command.SendMessage
import kz.btsd.messenger.bot.api.model.command.UiState
import kz.btsd.messenger.bot.api.model.media.InputMedia
import kz.btsd.messenger.bot.api.model.media.InputMedia.InputMediaType
import kz.btsd.messenger.bot.api.model.peer.PeerUser
import kz.btsd.messenger.bot.api.model.update.InlineCommandSelected
import kz.btsd.messenger.bot.api.model.update.Message
import kz.btsd.messenger.bot.api.model.update.Update
import kz.btsd.messenger.bot.api.model.upload.UploadedFile
import kz.btsd.messenger.bot.api.model.upload.UploadedFileResponse
import mu.KotlinLogging.logger
import org.springframework.beans.factory.annotation.Autowired
import java.io.File
import java.util.UUID
import kotlin.collections.HashMap
import kotlin.coroutines.resume

const val DEFAULT_REPLY_KEYBOARD_KEY = "DEFAULT_REPLY_KEYBOARD"
const val INLINE_KEYBOARD_PREFIX_LENGTH = 5

abstract class Dispatcher(
        apiUrl: String,
        private val token: String,
        scenarioFactory: ScenarioFactory
) : LongPollingBot(token, apiUrl) {

    private val log = logger {}
    private val mapper = jacksonObjectMapper().findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @Autowired
    private var sessionService: SessionService? = null

    private val botScenariosFactory = scenarioFactory.forBot(this.javaClass)
    protected val handlers = HashMap<String, Handler>()
    protected val sessions = HashMap<String, Session>()

    override fun onUpdate(update: Update) {
        log.trace { "Received update: $update" }
        log.trace { "Start to handle update: $update" }
        GlobalScope.launch {
            log.trace { "Launched coroutine for update: $update" }
            val messengerId = update.messengerId
            log.trace { "MessengerId = $messengerId" }
            try {
                val keyword = (update as? Message)
                        ?.takeIf { botScenariosFactory.isScenarioKeyword(it.content) }
                        ?.content
                log.trace { "keyword = $keyword" }
                when {
                    keyword != null -> {
                        log.trace { "Starting scenario = $keyword" }
                        startScenario(update, keyword)
                    }
                    handlers.containsKey(messengerId) -> {
                        log.trace { "Invoking handler = ${handlers[messengerId]}" }
                        handlers[messengerId]!!.invoke(update)
                    }
                    else -> {
                        log.trace { "Invoking onUnexpectedUpdate for update = $update" }
                        onUnexpectedUpdate(update)
                    }
                }
            } catch (e: ResponseExtractionException) {
                log.trace(e) { "Ignoring response extraction error" }
            } catch (e: Exception) {
                log.error(e) { }
                onException(e, messengerId)
            }
        }
    }

    protected open fun onException(e: Exception, messengerId: String) {
        sessions.remove(messengerId)
        sessionService?.deleteSession(token, messengerId)
        handlers.remove(messengerId)
        throw e
    }

    protected open suspend fun onUnexpectedUpdate(update: Update) {
        sessionService?.also { getSessionIfExists(update) }
    }

    protected open suspend fun getSessionIfExists(update: Update) {
        val messengerId = update.messengerId
        val session = sessionService?.getSession(token, messengerId) ?: initSession(update)
        sessions[messengerId] = session
        val keyword = session["lastScenario"]
        if (keyword != null) {
            startScenario(update, "$keyword")
        }
    }

    protected open suspend fun initSession(update: Update): Session {
        return if (sessionService == null) {
            Session(update.messengerId)
        } else {
            SessionStored(update.messengerId, token, sessionService)
        }
    }

    fun dropSession(messengerId: String) {
        sessions.remove(messengerId)
        sessionService?.deleteSession(token, messengerId)
    }

    open suspend fun startScenario(update: Update, keyword: String) {
        val messengerId = update.messengerId
        val session = sessions[messengerId] ?: sessionService?.getSession(token, messengerId) ?: initSession(update)
        sessions[messengerId] = session
        val scenario = botScenariosFactory.createScenario(keyword, session)
        session["lastScenario"] = keyword
        scenario.start(update)
    }

    suspend fun <T> sendRequest(
            command: Command,
            responseExtractionError: String?,
            responseExtractor: ResponseExtractor<T>
    ): T {
        return suspendCancellableCoroutine { continuation ->
            handlers[command.messengerId] = { update ->
                try {
                    val response = responseExtractor(update)
                    try {
                        handlers.remove(command.messengerId)
                        continuation.resume(response)
                    } catch (e: IllegalStateException) {
                        log.trace(e) { }
                        /**
                         * skipping "coroutine already resumed" exception,
                         * because it could possibly be triggered by double button taps
                         */
                    }
                } catch (e: Exception) {
                    if (responseExtractionError != null) {
                        sendMessage(content = responseExtractionError, messengerId = update.messengerId)
                    }
                    throw ResponseExtractionException(e)
                }
            }
            sendCommand(command)
        }
    }

    suspend fun <T> sendRequest(
            text: String,
            messengerId: String,
            localId: String? = null,
            replyToMessageId: String? = null,
            inlineUniqueKeyboard: InlineUniqueKeyboard? = null,
            uiState: UiState? = null,
            mediaList: List<InputMedia> = emptyList(),
            responseExtractionError: String? = null,
            responseExtractor: ResponseExtractor<T>
    ): T {

        val keyboardWithId = inlineUniqueKeyboard?.let {
            // full UUID might not fit into the callback data
            inlineUniqueKeyboard.withId(UUID.randomUUID().toString().substring(0..INLINE_KEYBOARD_PREFIX_LENGTH))
        }


        val command = buildSendMessage(
                content = text,
                messengerId = messengerId,
                localId = localId,
                replyToMessageId = replyToMessageId,
                inlineCommands = keyboardWithId?.inlineCommands ?: inlineUniqueKeyboard?.inlineCommands,
                mediaList = mediaList,
                uiState = uiState)

        return sendRequest(command, responseExtractionError) { update ->
            extractResponse(update, keyboardWithId, responseExtractor)
        }
    }

    private suspend fun <T> extractResponse(
            update: Update,
            keyboardWithId: InlineUniqueKeyboard?,
            responseExtractor: ResponseExtractor<T>
    ): T = try {
        if (keyboardWithId != null && update is InlineCommandSelected) {
            responseExtractor(keyboardWithId.validateAndStripId(update))
        } else {
            responseExtractor(update)
        }
    } catch (e: Exception) {
        throw ResponseExtractionException(e)
    }

    fun sendMessage(content: String,
                    messengerId: String,
                    localId: String? = null,
                    replyToMessageId: String? = null,
                    inlineCommands: List<InlineCommand>? = null,
                    uiState: UiState? = null,
                    mediaList: List<InputMedia> = emptyList()) {

        sendCommand(buildSendMessage(messengerId, localId, replyToMessageId, content, inlineCommands, uiState,
                mediaList))

    }

    fun sendUiState(messengerId: String, uiState: UiState) {
        sendCommand(buildSendUiState(messengerId, uiState))
    }

    fun sendFile(
            fileId: String,
            fileTitle: String,
            inputMediaType: InputMediaType,
            messengerId: String,
            inlineCommands: List<InlineCommand>? = null,
            uiState: UiState? = null
    ) {
        sendCommand(
                SendMessage(
                        recipient = PeerUser(messengerId),
                        content = "",
                        inlineCommands = inlineCommands ?: emptyList(),
                        mediaList = listOf(InputMedia(fileId, fileTitle, inputMediaType)),
                        uiState = uiState
                )
        )
    }

    fun uploadFilesMap(files: Map<File, String>): List<UploadedFile> {
        val response = mapper.readValue(uploadFiles(files).bytes(), UploadedFileResponse::class.java)
        return response.uploadedFiles
    }

    suspend inline fun <reified T : Enum<T>> sendEnumRequest(
            messengerId: String,
            text: String,
            uiState: UiState? = null,
            responseExtractionError: String? = null,
            noinline labelProvider: LabelProvider<T>
    ): T = sendRequest(
            text = text,
            messengerId = messengerId,
            inlineUniqueKeyboard = enumInlineUniqueKeyboard(labelProvider = labelProvider),
            responseExtractor = parseEnum(),
            uiState = uiState,
            responseExtractionError = responseExtractionError
    )

    suspend fun <T> selectionList(
            messengerId: String,
            items: List<T>,
            idProvider: IdProvider<T>,
            message: String,
            emptyMessage: String,
            validationErrorMessage: String,
            inlineUniqueKeyboard: InlineUniqueKeyboard,
            selectionMatcher: SelectionMatcher<T>? = null
    ): T? {
        val matcher = selectionMatcher ?: { item, selectedValue -> idProvider(item) == selectedValue }
        val messageText = if (items.isEmpty()) emptyMessage else message
        return sendRequest(
                text = messageText,
                messengerId = messengerId,
                inlineUniqueKeyboard = inlineUniqueKeyboard,
                responseExtractionError = validationErrorMessage,
                responseExtractor = { update ->
                    items.find {
                        matcher(it, (update as InlineCommandSelected).metadata.toLong())
                    }
                }
        )
    }


    private fun copyUiState(uiState: UiState,
                            canWriteText: Boolean? = null,
                            showCameraButton: Boolean? = null,
                            showShareContactButton: Boolean? = null,
                            showRecordAudioButton: Boolean? = null,
                            showGalleryButton: Boolean? = null,
                            replyKeyboard: List<ReplyCommand>? = null,
                            quickButtonCommands: List<QuickButtonCommand>? = null,
                            formMessage: FormMessage? = null,
                            interfaceView: InterfaceView? = null
    ): UiState {
        return UiState(canWriteText = canWriteText ?: uiState.canWriteText,
                showCameraButton = showCameraButton ?: uiState.showCameraButton ?: true,
                showShareContactButton = showShareContactButton ?: uiState.showShareContactButton,
                showRecordAudioButton = showRecordAudioButton ?: uiState.showRecordAudioButton,
                showGalleryButton = showGalleryButton ?: uiState.showGalleryButton,
                replyKeyboard = replyKeyboard ?: uiState.replyKeyboard,
                quickButtonCommands = quickButtonCommands ?: uiState.quickButtonCommands,
                formMessage = formMessage ?: uiState.formMessage,
                interfaceView = interfaceView ?: uiState.interfaceView)
    }
}

typealias Handler = suspend (update: Update) -> Unit
typealias ResponseExtractor<T> = suspend (update: Update) -> T

class ResponseExtractionException(cause: Exception) : RuntimeException(cause)
