package kz.btsd.bot.botscenariosdk.i18n

val <T : Enum<T>> T.translationKey get() = "${declaringClass.simpleName}.$name"
