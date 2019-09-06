package kz.btsd.bot.botscenariosdk.utils

import kz.btsd.bot.botscenariosdk.ResponseExtractor
import kz.btsd.messenger.bot.api.model.update.InlineCommandSelected

inline fun <reified T : Enum<T>> parseEnum(): ResponseExtractor<T> =
    { update -> enumValueOf((update as InlineCommandSelected).metadata) }

// When it's required that user presses a button, but the result is not needed
val requireMetadata: ResponseExtractor<Unit> = { update ->
    (update as? InlineCommandSelected)?.metadata
        ?: throw IllegalArgumentException("Update should be an inline command")
}
