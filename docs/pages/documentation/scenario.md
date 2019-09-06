---
title: Scenario
sidebar: home_sidebar
keywords: scenario
permalink: scenario.html
toc: true
folder: documentation
---
## Сценарий

Сценарий - это класс, определяющий последовательность действий в ответ на ввод пользователем команды, начинающейся на `/` или ключевого слова

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

Для создания сценария, необходимо:
 - расширить класс `SessionAwareOperations` с указанием диспетчера в параметрах конструктора
 - реализовать интерфейс `ScenarioEntryPoint`
 - пометить класс аннотацией `@Scenario` с указанием команды и ключевого слова, в ответ на которые будет запущен метод `start`
 
## Внешние сценарии

Реакцией на действие пользователя может быть старт нового сценария. В примере ниже, из сценария `/help` при клике по кнопке `Start` инициируется выполнение сценария `/start`
```kotlin
@Scenario(DemoDispatcher::class, "/help", "help")
class HelpScenario(
        dispatcher: DemoDispatcher,
        val startScenario: StartScenario
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    override fun init(session: Session) {
        super.init(session)
        startScenario.init(session)
    }

    enum class HelpOption(val title: String) {
        START("\uD83D\uDCCD Start")
    }

    override suspend fun start(update: Update) {
        val helpOption: HelpOption = sendEnumRequest("Помогу стартовать сценарий start") { it.title }
        when (helpOption) {
            HelpOption.START -> startScenario.start(update);
        }
    }

}
```  

![message](images/outer-scenario.png "outer-scenario")
