---
title: Session
sidebar: home_sidebar
keywords: session
permalink: session.html
toc: true
folder: documentation
---
## Session

session - это объект класса Session для хранения контекста в рамках взаимодействия с пользователем. Для каждого пользователя храниться своя сессия в единственном экземпляре.
Session реализует интерфейс Map и хранит пары ключ-значение. Во все объекты Session при создании помещается идентификатор пользователя с ключом `messengerId`
Объекты Session доступны в сценариях. Пример работы с Session:
```kotlin
const val COUNTER_KEY = "counter"

@Scenario(DemoDispatcher::class, "/session", "session")
class SessionScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    override suspend fun start(update: Update) {
        // get messengerId
        val messengerId = session[messengerId]
        // put value in session
        session.putIfAbsent(COUNTER_KEY, 0)
        session[COUNTER_KEY]=session[COUNTER_KEY] as Int + 1
        val counterValue = session[COUNTER_KEY] as Int
        sendMessage("Counter value: $counterValue")
    }
}
```

Доступ к session из другого сценария:
```kotlin
@Scenario(DemoDispatcher::class, "/session2", "session2")
class Session2Scenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    override suspend fun start(update: Update) {
        sendMessage("Counter value from session2 scenario: $session[$COUNTER_KEY]")
    }
}
```

![message](images/session.png "session")
