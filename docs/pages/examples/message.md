---
title: Message
sidebar: home_sidebar
keywords: message
permalink: message.html
toc: true
folder: examples
---
# Message

![message](images/message.png "message")

Пример обмена сообщениями между сервисом и пользователем:
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
