---
title: Dispatcher
sidebar: home_sidebar
keywords: dispatcher
permalink: dispatcher.html
toc: true
folder: documentation
---
## Dispatcher

Это класс, обеспечивающий функциональность обмена сообщениями между сервисом и пользователем посредством [Сервис-платформы](TODO!)
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
   
Для создания диспетчера сервиса необходимо расширить базовый класс Dispatcher и передать следующие параметры в конструктор: 
- botApiUrl
- token
- scenarioFactory

Рекомендуется использовать возможности Spring для создания и внедрения зависимостей и значений свойств. В примере выше, 
`scenarioFactory` создается и внедряется посредством Spring DI, а свойства `botApiUrl` и `token` определяются в application.properties

```
    bot.api.url=https://messapi.btsdapps.net/bot/v1/
    token=36e5c8-dda6-11e8-9f8b-f2801f1bfake
   ```
