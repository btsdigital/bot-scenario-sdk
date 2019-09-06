---
title: Command
sidebar: home_sidebar
keywords: command
permalink: command.html
toc: true
folder: documentation
---
# Command
## Сообщение и запрос
Command отправляется сервисом пользователю. Более подробная информация содержится в разделе [Как это работает](#hot-it-works.md).
Полный список поддерживаемых команд и их свойств можно найти в описании [Сервис-платформы](TODO)

Команду можно отправить двумя способами:
- не ожидая ответа от пользователя
- ожидая ответ от пользователя

В первом случае отправляется простое текстовое сообщение
```kotlin
sendMessage("Hello from demo service")
```

Использование в сценарии:
```kotlin
@Scenario(DemoDispatcher::class, "/start", "start")
class StartScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    override suspend fun start(update: Update) {
        sendMessage("Hello from demo service")
    }
}
```

Во втором случае отправляется запрос на ввод данных. В этом случае необходимо использовать метод `sendRequest`
```kotlin
sendRequest(SendMessage(recipient = Peer(Peer.Type.USER, messengerId), content = "Enter text")) {}
```
Использование в сценарии:
```kotlin
@Scenario(DemoDispatcher::class, "/sendResponseMessage", "sendResponseMessage")
class SendResponseMessageScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    override suspend fun start(update: Update) {
        sendRequest(SendMessage(recipient = Peer(Peer.Type.USER, messengerId), content = "Enter text")) {
            sendMessage("Sent: ${(it as Message).content}")
        }
    }
}
```

## Обработка ответа
Ответ на запрос приходит в виде объекта [Update](#Update) и обрабатывается в `responseExtractor`. 
Передать `responseExtractor` в метод `sendRequest` можно, заключив код в фигурные скобки, следующие после параметров метода:
```kotlin
sendRequest(SendMessage(recipient = Peer(Peer.Type.USER, messengerId), content = "Enter text")) {
    sendMessage("Sent: ${(it as Message).content}")
}
```

Использование в сценарии:
```kotlin
@Scenario(DemoDispatcher::class, "/sendResponseMessage", "sendResponseMessage")
class SendResponseMessageScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    override suspend fun start(update: Update) {
        sendRequest(SendMessage(recipient = Peer(Peer.Type.USER, messengerId), content = "Enter text")) {
            sendMessage("Sent: ${(it as Message).content}")
        }
    }
}
```

Возвращаемое значение `responseExtractor` используется в качестве возвращаемого значения метода `sendRequest`
```kotlin
@Scenario(DemoDispatcher::class, "/simpleResponseExtractor", "simpleResponseExtractor")
class SimpleResponseExtractorScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    override suspend fun start(update: Update) {
        val response = sendRequest(SendMessage(recipient = Peer(Peer.Type.USER, messengerId), 
                                    content = "Enter text")) { it }
        sendMessage(response.toString())
    }
}
```
В данном случае, `response` будет инициализирован объектом `Update` ответа пользователя на запрос

## Обработка некорректного ввода
В случае необходимости валидации ввода пользователя, необходимо передать `validationErrorMessage` в метод `sendRequest`.
 Текст валидационного сообщения будет отправлен пользователю в случае возникновения ошибки в блоке обработки ответа `responseExtractor`
```kotlin
@Scenario(DemoDispatcher::class, "/validationMessage", "validationMessage")
class ValidationErrorMessageScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d.MM.yyyy")

    override suspend fun start(update: Update) {
        sendRequest(SendMessage(recipient = Peer(Peer.Type.USER, messengerId), 
                                content = "Enter date in format dd.mm.yyyy"),
                    validationErrorMessage = "Please, enter valid date in format dd.mm.yyyy") {
            if (it is Message) {
                val date = LocalDate.parse(it.content, DATE_TIME_FORMATTER)
                sendMessage("Entered: ${date.format(DATE_TIME_FORMATTER)}")
            } else {
                throw UnsupportedOperationException()
            }
        }
    }
}
```