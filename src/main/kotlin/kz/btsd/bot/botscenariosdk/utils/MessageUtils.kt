package kz.btsd.bot.botscenariosdk.utils

fun indexMessage(header: String, entries: List<String>) = entries
    .mapIndexed { i, entry -> "${i + 1}. $entry" }
    .joinLines(prefix = "$header${System.lineSeparator()}${System.lineSeparator()}")
