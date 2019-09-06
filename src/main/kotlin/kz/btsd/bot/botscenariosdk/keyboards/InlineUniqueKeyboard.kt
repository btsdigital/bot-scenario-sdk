package kz.btsd.bot.botscenariosdk.keyboards

import kz.btsd.messenger.bot.api.model.command.InlineCommand
import kz.btsd.messenger.bot.api.model.update.InlineCommandSelected

class InlineUniqueKeyboard(val inlineCommands: List<InlineCommand>, val id: String = "") {
    constructor(vararg inlineCommands: InlineCommand) : this(inlineCommands.toList())

    fun withId(id: String) = InlineUniqueKeyboard(
            inlineCommands = inlineCommands.map { it.copy(metadata = "$id:${it.metadata}") },
            id = id
    )

    fun validateAndStripId(inlineCommandSelected: InlineCommandSelected): InlineCommandSelected {
        return inlineCommandSelected.copy(metadata = extractData(inlineCommandSelected.metadata))
    }

    private fun extractData(metadata: String) = metadata
            .takeIf { it.startsWith(id) }
            ?.substring(id.length + 1)
            ?: throw IllegalArgumentException("Cannot extract a keyboard id=$id, from metadata=$metadata")
}
