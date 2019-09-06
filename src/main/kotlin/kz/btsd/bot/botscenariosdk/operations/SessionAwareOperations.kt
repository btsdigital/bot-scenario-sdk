package kz.btsd.bot.botscenariosdk.operations

import kz.btsd.bot.botscenariosdk.Dispatcher
import kz.btsd.bot.botscenariosdk.ResponseExtractor
import kz.btsd.bot.botscenariosdk.config.ScenarioSdkConfig
import kz.btsd.bot.botscenariosdk.keyboards.InlineUniqueKeyboard
import kz.btsd.bot.botscenariosdk.session.Session
import kz.btsd.bot.botscenariosdk.utils.IdProvider
import kz.btsd.bot.botscenariosdk.utils.Identifiable
import kz.btsd.bot.botscenariosdk.utils.LabelProvider
import kz.btsd.bot.botscenariosdk.utils.SelectionMatcher
import kz.btsd.messenger.bot.api.model.command.Command
import kz.btsd.messenger.bot.api.model.command.InlineCommand
import kz.btsd.messenger.bot.api.model.command.SendMessage
import kz.btsd.messenger.bot.api.model.command.SendUiState
import kz.btsd.messenger.bot.api.model.command.UiState
import kz.btsd.messenger.bot.api.model.media.InputMedia
import kz.btsd.messenger.bot.api.model.media.InputMedia.InputMediaType
import kz.btsd.messenger.bot.api.model.update.Update
import kz.btsd.bot.botscenariosdk.utils.builder.buildSendUiState
import kz.btsd.bot.botscenariosdk.utils.builder.buildSendMessage
import kz.btsd.messenger.bot.api.model.command.SendContainerMessage
import kz.btsd.messenger.bot.api.model.peer.PeerUser
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct


open class SessionAwareOperations(val dispatcher: Dispatcher) {

    @Autowired
    lateinit var scenarioSdkConfig: ScenarioSdkConfig

    lateinit var session: Session
    lateinit var messengerId: String
    open lateinit var defaultErrorMessage: String
    open lateinit var defaultEnumErrorMessage: String

    @PostConstruct
    fun postConstruct() {
        defaultErrorMessage = scenarioSdkConfig.defaultErrorMessage
        defaultEnumErrorMessage = scenarioSdkConfig.defaultRequestEnumErrorMessage
    }

    open fun init(session: Session) {
        this.session = session
        this.messengerId = session.messengerId
    }

    fun dropSession() {
        dispatcher.dropSession(messengerId)
    }

    fun sendMessage(text: String,
                    localId: String? = null,
                    replyToMessageId: String? = null,
                    inlineCommands: List<InlineCommand> = emptyList(),
                    uiState: UiState? = null,
                    mediaList: List<InputMedia> = emptyList()) {
        dispatcher.sendMessage(content = text,
                messengerId = messengerId,
                localId = localId,
                replyToMessageId = replyToMessageId,
                inlineCommands = inlineCommands,
                uiState = uiState,
                mediaList = mediaList)
    }

    fun sendFile(fileId: String, fileTitle: String, inputMediaType: InputMediaType, messengerId: String? = null) {
        dispatcher.sendFile(fileId, fileTitle, inputMediaType, messengerId ?: this.messengerId)
    }

    fun sendUiState(uiState: UiState) {
        dispatcher.sendUiState(messengerId, uiState = uiState)
    }

    fun buildSendMessage(content: String,
                         localId: String? = null,
                         replyToMessageId: String? = null,
                         inlineCommands: List<InlineCommand>? = null,
                         uiState: UiState? = null,
                         mediaList: List<InputMedia> = emptyList()): SendMessage {
        return buildSendMessage(messengerId, localId, replyToMessageId, content, inlineCommands, uiState, mediaList)
    }

    fun buildSendUiState(uiState: UiState): SendUiState {
        return buildSendUiState(messengerId, uiState = uiState)
    }

    fun buildSendContainerMessage(content: String, localId: String? = null): SendContainerMessage {
        return SendContainerMessage(localId, PeerUser(messengerId), content)
    }

    fun sendContainerMessage(content: String, localId: String? = null) {
        dispatcher.sendCommand(buildSendContainerMessage(content, localId))
    }

    suspend inline fun <reified T : Enum<T>> sendEnumRequest(
        text: String,
        uiState: UiState? = null,
        validationErrorMessage: String? = defaultEnumErrorMessage,
        noinline labelProvider: LabelProvider<T>
        ): T {
        return dispatcher.sendEnumRequest(messengerId, text, uiState, validationErrorMessage, labelProvider)
    }

    suspend fun <T> showSelectionList(
            items: List<T>,
            idProvider: IdProvider<T>,
            message: String,
            emptyMessage: String,
            validationErrorMessage: String = defaultErrorMessage,
            inlineUniqueKeyboard: InlineUniqueKeyboard,
            selectionMatcher: SelectionMatcher<T>? = null
    ): T? {
        return dispatcher.selectionList(
                messengerId = messengerId,
                items = items,
                idProvider = idProvider,
                message = message,
                emptyMessage = emptyMessage,
                validationErrorMessage = validationErrorMessage,
                inlineUniqueKeyboard = inlineUniqueKeyboard,
                selectionMatcher = selectionMatcher
        )
    }


    suspend fun <T : Identifiable> showSelectionList(
            items: List<T>,
            message: String,
            emptyMessage: String,
            validationErrorMessage: String = defaultErrorMessage,
            inlineUniqueKeyboard: InlineUniqueKeyboard,
            selectionMatcher: SelectionMatcher<T>? = null
    ): T? {
        return dispatcher.selectionList(
                messengerId = messengerId,
                items = items,
                idProvider = { it.id },
                message = message,
                emptyMessage = emptyMessage,
                validationErrorMessage = validationErrorMessage,
                inlineUniqueKeyboard = inlineUniqueKeyboard,
                selectionMatcher = selectionMatcher
        )
    }


    suspend fun <T> sendRequest(
            text: String,
            inlineUniqueKeyboard: InlineUniqueKeyboard? = null,
            validationErrorMessage: String? = defaultErrorMessage,
            uiState: UiState? = null,
            responseExtractor: ResponseExtractor<T>
    ): T {
        return dispatcher.sendRequest(
                messengerId = messengerId,
                text = text,
                inlineUniqueKeyboard = inlineUniqueKeyboard,
                responseExtractionError = validationErrorMessage,
                uiState = uiState,
                responseExtractor = responseExtractor
        )
    }

    suspend fun <T> sendRequest(
            command: Command,
            validationErrorMessage: String? = defaultErrorMessage,
            responseExtractor: ResponseExtractor<T>
    ): T {
        return dispatcher.sendRequest(
            command = command,
            responseExtractionError = validationErrorMessage,
            responseExtractor = responseExtractor
        )
    }

    suspend fun startScenario(update: Update, keyword: String) {
        dispatcher.startScenario(update, keyword)
    }
}
