---
title: Inline command
sidebar: home_sidebar
keywords: inline, command
permalink: inline-command.html
toc: true
folder: examples
---
# Inline command

<img src="images/inline-command.png" height="50%">

В случае, когда необходимо сделать выбор из доступных опций в виде `InlineCommand`, можно использовать метод `sendEnumRequest<T>`
```kotlin
@Scenario(DemoDispatcher::class, "/inlineCommand", "inlineCommand")
class InlineCommandScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    enum class DateOption(val title: String) {
        TODAY("\uD83D\uDCC5 Сегодня"),
        TOMORROW("\uD83D\uDCC6 Завтра")
    }

    override suspend fun start(update: Update) {
        val dateOption = sendEnumRequest<DateOption>("Choose date option") { it.title }
        sendMessage("Chosen option: ${dateOption.title}")
    }
}
```

Если в качестве ответа на команду, содержащую `InlineCommand`, ожидается ответ, отличный от `InlineCommandSelected`, 
необходимо использовать метод `sendRequest` и обрабатывать все возможные типы ответов:
```kotlin
@Scenario(DemoDispatcher::class, "/update", "update")
class UpdateScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy")

    enum class DateOption(val title: String) {
        TODAY("\uD83D\uDCC5 Сегодня"),
        TOMORROW("\uD83D\uDCC6 Завтра")
    }

    override suspend fun start(update: Update) {
        val keyboard: Keyboard = InlineKeyboard(listOf(
                InlineCommand(DateOption.TODAY.title, DateOption.TODAY.name),
                InlineCommand(DateOption.TOMORROW.title, DateOption.TOMORROW.name)))

        val date: LocalDate = sendRequest("Choose date option or enter date in format dd.mm.yyyy", keyboard,
                "Please, choose an option or enter valid date in format dd.mm.yyyy") {
            when (it) {
                is Message -> LocalDate.parse(it.content, dateTimeFormatter)
                is InlineCommandSelected -> when (DateOption.valueOf(it.metadata)) {
                    DateOption.TODAY -> LocalDate.now()
                    DateOption.TOMORROW -> LocalDate.now().plus(1, ChronoUnit.DAYS)
                }
                else -> throw UnsupportedOperationException()
            }
        }
        sendMessage("Entered: ${date.format(dateTimeFormatter)}")
    }
}
```
