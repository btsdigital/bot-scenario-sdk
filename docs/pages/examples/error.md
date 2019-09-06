---
title: Error
sidebar: home_sidebar
keywords: error
permalink: error.html
toc: true
folder: examples
---

Пример обработки ошибок в методах `sendRequest` и `sendEnumRequest`:
```kotlin
@Scenario(DemoDispatcher::class, "/error", "error")
class ErrorScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    enum class ErrorOption(val title: String) {
        REQUEST_ERROR_DEFAULT("REQUEST_ERROR_DEFAULT"),
        REQUEST_ERROR_CUSTOM("REQUEST_ERROR_CUSTOM"),
        ENUM_REQUEST_ERROR_DEFAULT("ENUM_REQUEST_ERROR_DEFAULT"),
        ENUM_REQUEST_ERROR_CUSTOM("ENUM_REQUEST_ERROR_CUSTOM")
    }

    enum class OkOption(val title: String)

    override suspend fun start(update: Update) {
        when (sendEnumRequest<ErrorOption>("Помогу стартовать сценарий start") { it.title }) {
            ErrorOption.REQUEST_ERROR_DEFAULT -> sendRequest("Sending error request without validationErrorMessage. " +
                    "Should receive default error message. Write me anything") { throw RuntimeException("demo exception") }
            ErrorOption.REQUEST_ERROR_CUSTOM -> sendRequest(text = "Sending error request with validationErrorMessage = Custom error message. " +
                    "Should receive 'Custom error message'. Write me anything", validationErrorMessage = "Custom error message") { throw RuntimeException("demo exception") }
            ErrorOption.ENUM_REQUEST_ERROR_DEFAULT -> sendEnumRequest<OkOption>("Sending error enumRequest without validationErrorMessage. " +
                    "Should receive default enum error message. Write me anything") { it.title }
            ErrorOption.ENUM_REQUEST_ERROR_CUSTOM -> sendEnumRequest<OkOption>("Sending error enumRequest with validationErrorMessage = Custom enum error message. " +
                    "Should receive 'Custom enum error message'. Write me anything", validationErrorMessage = "Custom enum error message") { it.title }
        }
    }
}
```

## Ошибка по умолчанию в методе `sendRequest`

В случае выбора опции REQUEST_ERROR_DEFAULT, отправляется запрос пользователю с помощью метода `sendRequest` без указания
текста ошибки в параметре `validationErrorMessage`. В этом случае будет выдан текст ошибки, содержащийся
в опции `sdk.scenario.defaultErrorMessage` [конфигурации](#configuration.md) `bot-scenario-sdk`

<img src="images/defaultError.png" height="50%">

## validationErrorMessage в методе `sendRequest`

В случае выбора опции REQUEST_ERROR_CUSTOM, отправляется запрос пользователю с помощью метода `sendRequest` с указанием
текста ошибки в параметре `validationErrorMessage`. В этом случае будет выдан текст ошибки, указанный в параметре `validationErrorMessage`

<img src="images/customError.png" height="50%">

## Ошибка по умолчанию в методе `sendEnumRequest`

В случае выбора опции ENUM_REQUEST_ERROR_DEFAULT, отправляется запрос пользователю с помощью метода `sendEnumRequest` без указания
текста ошибки в параметре `validationErrorMessage`. В этом случае будет выдан текст ошибки, содержащийся
в опции `sdk.scenario.defaultRequestEnumErrorMessage` [конфигурации](#configuration.md) `bot-scenario-sdk`

<img src="images/defaultEnumError.png" height="50%">

## validationErrorMessage в методе `sendEnumRequest`

В случае выбора опции ENUM_REQUEST_ERROR_CUSTOM, отправляется запрос пользователю с помощью метода `sendEnumRequest` с указанием
текста ошибки в параметре `validationErrorMessage`. В этом случае будет выдан текст ошибки, указанный в параметре `validationErrorMessage`

<img src="images/customEnumError.png" height="50%">