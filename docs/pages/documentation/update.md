---
title: Update
sidebar: home_sidebar
keywords: update
permalink: update.html
toc: true
folder: documentation
---
## Update
Сервис получает сообщения о действиях пользователя и обновлениях системы в виде объектов `Update`.
Более подробная информация содержится в разделе [Как это работает](#hot-it-works.md).

В качестве `Update` может придти любой поддерживаемый [Сервис-платформой](TODO) подтип класса `Update`. В том числе:
- Message
- InlineCommandSelected
- QuickButtonSelected
- FormSubmitted
- FormClosed
- FormMessageSent
- ChannelSubscribed
- InvitedToGroup
- KickedFromGroup
- MessageIdAssigned

Полный список поддерживаемых `Update` и их свойств можно найти в описании [Сервис-платформы](TODO)

Пример обработки `Update`
```kotlin
@Scenario(DemoDispatcher::class, "/update", "update")
class UpdateScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy")

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
Сервис посылает запрос на ввод даты в формате `dd.mm.yyyy` или выбор из опций [Сегодня, Завтра]. 
В случае выбора пользователем опции, в качестве ответа приходит объект типа `InlineCommandSelected`. 
В случае ввода пользователем текста, в качестве ответа приходит объект типа `Message`. 
Ответ обрабатывается в зависимости от типа полученного сообщения

