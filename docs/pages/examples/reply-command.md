---
title: Reply command
sidebar: home_sidebar
keywords: reply, reply-command
permalink: reply-command.html
toc: true
folder: examples
---
# Reply command

![reply-command](images/reply-command.png "reply-command")

Пример использования `ReplyCommand`
```kotlin
@Scenario(DemoDispatcher::class, "/replyCommand", "replyCommand")
class ReplyCommandScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy")

    enum class DateOption(val title: String) {
        TODAY("\uD83D\uDCC5 Сегодня"),
        TOMORROW("\uD83D\uDCC6 Завтра")
    }

    override suspend fun start(update: Update) {
        val uiState = UiState(replyKeyboard = listOf(
                ReplyCommand(DateOption.TODAY.title),
                ReplyCommand(DateOption.TOMORROW.title)))

        val date: LocalDate = sendRequest(text = "Choose option from reply menu", uiState = uiState,
                validationErrorMessage = "Please, choose option from reply menu") {
            when (it) {
                is Message -> when (it.content) {
                    DateOption.TODAY.title -> LocalDate.now()
                    DateOption.TOMORROW.title -> LocalDate.now().plus(1, ChronoUnit.DAYS)
                    else -> throw UnsupportedOperationException()
                }
                else -> throw UnsupportedOperationException()
            }
        }
        sendMessage("Entered: ${date.format(dateTimeFormatter)}")
    }
}
```