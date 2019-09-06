package kz.btsd.bot.botscenariosdk.utils

import kz.btsd.bot.botscenariosdk.keyboards.InlineUniqueKeyboard
import kz.btsd.messenger.bot.api.model.command.InlineCommand

inline fun <reified T : Enum<T>> enumInlineUniqueKeyboard(
        values: List<T> = enumValues<T>().toList(),
        noinline labelProvider: LabelProvider<T>
) = InlineUniqueKeyboard(
        values.map { InlineCommand(labelProvider(it), it.name) }
)

fun <T> indexUniqueKeyboard(
        items: List<T>,
        idProvider: (item: T) -> Long,
        selectAllMessage: String? = null
): InlineUniqueKeyboard {
    val commands = items
            .mapIndexed { i, item -> InlineCommand((i + 1).toString(), idProvider(item).toString()) }
            .toMutableList()
    if (selectAllMessage != null && commands.isNotEmpty()) {
        commands.add(InlineCommand(selectAllMessage, Long.MIN_VALUE.toString()))
    }
    return InlineUniqueKeyboard(commands)
}

fun <T> idUniqueKeyboard(
        items: List<T>,
        idProvider: (item: T) -> Long,
        labelProvider: (value: T) -> String = { it.toString() }
): InlineUniqueKeyboard {
    return InlineUniqueKeyboard(
            items.map { InlineCommand(labelProvider(it), idProvider(it).toString()) }
    )
}

fun <T : Identifiable> idUniqueKeyboard(
        items: List<T>,
        labelProvider: (value: T) -> String = { it.toString() }
): InlineUniqueKeyboard {
    return InlineUniqueKeyboard(
            items.map { InlineCommand(labelProvider(it), it.id.toString()) }
    )
}
