package kz.btsd.bot.botscenariosdk.i18n

import java.io.InputStreamReader
import java.util.*

open class Translations(val languageCode: String) {
    private val properties: Properties = loadProperties()

    private fun loadProperties(): Properties {
        val inputStream = javaClass.getResourceAsStream("/i18n/$languageCode.properties")
        return Properties().apply {
            load(InputStreamReader(inputStream, Charsets.UTF_8))
        }
    }

    operator fun get(key: String): String {
        return properties.getProperty(key) ?: throw IllegalArgumentException("key=$key translation not found")
    }
}
