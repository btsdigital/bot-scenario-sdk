package kz.btsd.bot.botscenariosdk.utils

import java.lang.System.lineSeparator

fun <T> Array<out T>.joinLines(prefix: CharSequence = "", postfix: CharSequence = "") =
    joinToString(lineSeparator(), prefix, postfix)

fun <T> Iterable<T>.joinLines(prefix: CharSequence = "", postfix: CharSequence = "") =
    joinToString(lineSeparator(), prefix, postfix)
