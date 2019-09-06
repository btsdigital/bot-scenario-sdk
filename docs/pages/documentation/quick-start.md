---
title: Быстрый старт
sidebar: home_sidebar
keywords: quick, start
permalink: quick-start.html
toc: true
folder: documentation
---
 
 1) Создайте пустой [Spring Boot](http://spring.io/projects/spring-boot) проект любым удобным способом. Например, можно использовать следующие инструменты 
    - [Spring Initializr](https://start.spring.io/)
    - [IntelijIdea](https://www.javadevjournal.com/spring-boot/spring-boot-application-intellij/)
    - [command line](https://github.com/spring-io/initializr#generating-a-project)

    Пример создания пустого проекта при помощи командной строки:
   ```
    curl https://start.spring.io/starter.tgz -d language=kotlin -d type=gradle-project -d baseDir=bot-scenario-sdk-demo2 | tar -xzvf -
   ```
 2) После создания проекта нужно включить в зависимости bot-scenario-sdk. Для этого в файл build.gradle.kts в раздел dependencies нужно добавить:
   ```
    implementation("kz.btsd.bot:bot-scenario-sdk:${Version}")
   ```
 4) [Зарегистрируйте сервис](#регистрация-сервиса).
 5) Добавьте полученный токен и ссылку на [Сервис-платформу](TODO!) в application.properties
  ```
    bot.api.url=https://messapi.btsdapps.net/bot/v1/
    token=36e0c8-d996-11e8-9f8b-f2877fake
   ```
 6) Создайте в проекте класс [диспетчера](TODO!#диспетчер). Это класс, обеспечивающий функциональность обмена сообщениями между сервисом и пользователем посредством [Сервис-платформы](TODO!)
   ```kotlin
    @Component
    class DemoDispatcher(
            scenarioFactory: ScenarioFactory,
            @Value("\${bot.api.url}") botApiUrl: String,
            @Value("\${token}") token: String
    ) : Dispatcher(
            botApiUrl,
            token,
            scenarioFactory)
   ```
 7) Создайте в проекте класс [сценария](TODO!#сценарий) "/start"
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
 8) Выполните старт сервиса
```
   gradle bootRun
```
