package kz.btsd.bot.botscenariosdk.i18n

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Component

@Component
@ConditionalOnBean(Translations::class)
class TranslationService(private val translationsList: List<Translations>) {
    private val translationsByLanguageCode = translationsList.associateBy { it.languageCode }

    fun getTranslations(languageCode: String) = translationsByLanguageCode[languageCode]
        ?: throw IllegalArgumentException("Translations for $languageCode language not found")

    fun translate(languageCode: String, messageKey: String) = translationsByLanguageCode[languageCode]!![messageKey]

    fun allTranslations(messageKey: String) = translationsList.map { it[messageKey] }
}
