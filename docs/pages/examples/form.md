---
title: Form
sidebar: home_sidebar
keywords: form
permalink: form.html
toc: true
folder: examples
---
# Form

[Бэкдроп](TODO ссылка на UI описание компонента) - составной компонент для отображения форм внутри чатов.

![Form](images/form.png "Form")

Для использования форм, необходимо подключить зависимость

```kotlin
kz.btsd.messenger.bot:form-builder
```

```kotlin
@Scenario(DemoDispatcher::class, "/form", "form")
class FormScenario(
        dispatcher: DemoDispatcher) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    enum class QuickButton(val title: String) {
        SELECT_CURRENCY("Выбрать валюту")
    }

    private final val selectQB = QuickButtonCommand(QuickButton.SELECT_CURRENCY.title,
            action = QuickButtonCommand.QuickButtonAction.QUICK_REQUEST,
            metadata = QuickButton.SELECT_CURRENCY.name)

    private val form: Form = Form.of(ResourceReader.readContent("/FormMessage.json"))

    private final val emptyFormMessage = FormMessage("")

    val qbUiState = UiState(canWriteText = false,
            showCameraButton = false,
            showShareContactButton = false,
            showRecordAudioButton = false,
            showGalleryButton = false,
            replyKeyboard = emptyList(),
            quickButtonCommands = listOf(selectQB),
            formMessage = emptyFormMessage)

    val formUiState = UiState(canWriteText = false,
            showCameraButton = false,
            showShareContactButton = false,
            showRecordAudioButton = false,
            showGalleryButton = false,
            replyKeyboard = emptyList(),
            formMessage = FormMessage(form.toJson()))

    override suspend fun start(update: Update) {

        sendRequest(buildSendMessage("Демонстрация форм и quickButton", uiState = qbUiState)) { qbUpdate ->
            when (qbUpdate) {
                is QuickButtonSelected -> {
                    sendRequest(buildSendUiState(when (qbUpdate.metadata) {
                        QuickButton.SELECT_CURRENCY.name -> formUiState
                        else -> throw Exception("Unexpected type message")
                    })) { formUpdate ->
                        when (formUpdate) {
                            is FormMessageSent -> sendMessage("Значения на форме: ${formUpdate.message}. Сценарий завершен")
                            is FormClosed -> {
                                sendMessage("Форма закрыта. Сценарий завершен")
                            }
                        }
                    }
                }
                else -> throw Exception("Unexpected type message")
            }
        }

        sendUiState(defaultUiState) // set default UiState
    }
}
```
