package kz.btsd.bot.botscenariosdk.utils

typealias IdProvider<T> = (item: T) -> Long

typealias SelectionMatcher<T> = ((item: T, selectedId: Long) -> Boolean)

typealias LabelProvider<T> = (value: T) -> String
