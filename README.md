# bot-scenario-sdk

Проект предназначен для упрощения разработки [сервисов](TODO!#сервис) приложения [Мессенджер](TODO#Мессенджер)
## Быстрый старт
 
 1) Создайте пустой [Spring Boot](http://spring.io/projects/spring-boot) проект любым удобным способом. Например, можно использовать следующие инструменты 
    - [Spring Initializr](https://start.spring.io/)
    - [IntelijIdea](https://www.javadevjournal.com/spring-boot/spring-boot-application-intellij/)
    - [command line](https://github.com/spring-io/initializr#generating-a-project)
        
        curl https://start.spring.io/starter.tgz -d language=kotlin -d type=gradle-project -d baseDir=bot-scenario-sdk-demo2 | tar -xzvf -
 2) Включите в зависимости bot-scenario-sdk
     ```
         compile "kz.btsd.bot:bot-scenario-sdk:${Version}"
     ```
 4) [Зарегистрируйте сервис](#регистрация-сервиса). При регистрации, в списке доступных команд, укажите команду "/start"
 5) Добавьте полученный токен и ссылку на [Сервис-платформу](TODO!) в application.properties
     ```
        bot.api.url=https://messapi.btsdapps.net/bot/v1/
        token=registered_token
     ```
 6) Создайте [диспетчер](TODO!#диспетчер). Это класс, обеспечивающий функциональность обмена сообщениями между сервисом и пользователем посредством [Сервис-платформы](TODO!)
     ```
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
 7) Создайте [сценарий](TODO!#сценарий) "/start"
     ```
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
 
## Что дальше
Полная информация о возможностях библиотеки содержится в [документации](https://btsdigital.github.io/bot-scenario-sdk)

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## License

This project is licensed under the [Apache v2.0 License](https://github.com/btsdigital/bot-scenario-sdk/blob/master/LICENSE) file for details
