package kz.btsd.bot.botscenariosdk.operations

import kz.btsd.bot.botscenariosdk.Dispatcher
import kz.btsd.bot.botscenariosdk.ResponseExtractor
import kz.btsd.bot.botscenariosdk.i18n.Translations
import kz.btsd.bot.botscenariosdk.i18n.translationKey
import kz.btsd.bot.botscenariosdk.keyboards.InlineUniqueKeyboard
import kz.btsd.bot.botscenariosdk.session.Session
import kz.btsd.bot.botscenariosdk.utils.Identifiable
import kz.btsd.bot.botscenariosdk.utils.enumInlineUniqueKeyboard

const val TRANSLATIONS_KEY = "translations"
const val DEFAULT_INPUT_ERROR_MESSAGE_KEY = "inputError"
const val DEFAULT_SELECT_ALL_MESSAGE_KEY = "selectAll"

open class TranslationsAwareOperations(dispatcher: Dispatcher) : SessionAwareOperations(dispatcher) {
    protected lateinit var translations: Translations

    override fun init(session: Session) {
        super.init(session)
        translations = session[TRANSLATIONS_KEY] as Translations
    }

    suspend inline fun <reified T : Enum<T>> sendEnumRequest(text: String): T = sendEnumRequest(
        text = text,
        labelProvider = { it.translation }
    )

    suspend fun <T> sendRequest(
            text: String,
            inlineUniqueKeyboard: InlineUniqueKeyboard? = null,
            responseExtractor: ResponseExtractor<T>
    ): T {
        return sendRequest(
                text = text,
                inlineUniqueKeyboard = inlineUniqueKeyboard,
                validationErrorMessage = DEFAULT_INPUT_ERROR_MESSAGE_KEY.translation,
                responseExtractor = responseExtractor
        )
    }

    fun <T> indexUniqueKeyboard(
            items: List<T>,
            idProvider: (item: T) -> Long,
            selectAllOption: Boolean = false,
            selectAllMessage: String = DEFAULT_SELECT_ALL_MESSAGE_KEY.translation
    ): InlineUniqueKeyboard {
        return kz.btsd.bot.botscenariosdk.utils.indexUniqueKeyboard(
                items = items,
                idProvider = idProvider,
                selectAllMessage = if (selectAllOption) selectAllMessage else null
        )
    }

    fun <T : Identifiable> indexUniqueKeyboard(
            items: List<T>,
            selectAllOption: Boolean = false,
            selectAllMessage: String = DEFAULT_SELECT_ALL_MESSAGE_KEY.translation
    ): InlineUniqueKeyboard {
        return kz.btsd.bot.botscenariosdk.utils.indexUniqueKeyboard(
                items = items,
                idProvider = { it.id },
                selectAllMessage = if (selectAllOption) selectAllMessage else null
        )
    }

    inline fun <reified T : Enum<T>> enumUniqueKeyboard(
            values: List<T> = enumValues<T>().toList(),
            noinline labelProvider: (value: T) -> String = { it.translation }
    ): InlineUniqueKeyboard {
        return enumInlineUniqueKeyboard(values, labelProvider)
    }

    fun String.translation(vararg args: Any): String = translations[this].format(*args)

    val String.translation get() = translation()
    val <T : Enum<T>> T.translation get() = translationKey.translation
}
