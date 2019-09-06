---
title: Quick button
sidebar: home_sidebar
keywords: quick
permalink: quick-button.html
toc: true
folder: examples
---
# Quick button
	
[Компонент](TODO ссылка на UI описание компонента) для вывода кнопок действий с сервисом, а также кнопок для вызова нативных команд приложения.

![Quick_button](images/quick_button.png "Quick button")

```kotlin
@Scenario(DemoDispatcher::class, "/qb", "qb")
class QBScenario(dispatcher: DemoDispatcher) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    enum class QuickButton(val title: String) {
        ACTION("Действие"),
        FORM("Форма"),
        CALL("Звонок"),
        PHONE("Мой телефон"),
        PHONE_META("Мой телефон с метаданными"),
    }

    private final val actionQB = QuickButtonCommand(QuickButton.ACTION.title,
            action = QuickButtonCommand.QuickButtonAction.QUICK_REQUEST,
            metadata = QuickButton.ACTION.name)

    private final val formQB = QuickButtonCommand(QuickButton.FORM.title,
            action = QuickButtonCommand.QuickButtonAction.QUICK_REQUEST,
            metadata = QuickButton.FORM.name)

    private final val callQB = QuickButtonCommand("Call",
            QuickButtonCommand.QuickButtonAction.QUICK_FORM_ACTION,
            metadata = "{\n" +
            "\"action\" : \"redirect_call\",\n" +
            " \"data_template\" : \"There should be valid number\"\n" +
            "}")

    private final val phoneQB = QuickButtonCommand("Send my phone",
            QuickButtonCommand.QuickButtonAction.QUICK_FORM_ACTION,
            metadata = """
            {
              "action" : "send_private_data",
              "data_template" : "phone"
            }
        """.trimIndent())

    private final val phoneMetadataQB = QuickButtonCommand("Send my phone(with metadata)",
            QuickButtonCommand.QuickButtonAction.QUICK_FORM_ACTION,
            metadata = """
            {
              "action" : "send_private_data",
              "data_template" : "phone it is metadata"
            }
        """.trimIndent())


    val qbUiState = UiState(canWriteText = false,
            showCameraButton = false,
            showShareContactButton = false,
            showRecordAudioButton = false,
            showGalleryButton = false,
            replyKeyboard = emptyList(),
            quickButtonCommands = listOf(actionQB, formQB, callQB, phoneQB, phoneMetadataQB),
            formMessage = emptyFormMessage)

    val formUiState = UiState(canWriteText = false,
            showCameraButton = false,
            showShareContactButton = false,
            showRecordAudioButton = false,
            showGalleryButton = false,
            replyKeyboard = emptyList(),
            formMessage = FormMessage(form.toJson()))

    override suspend fun start(update: Update) {

        var command: Command = buildSendMessage("Демонстрация форм и quickButton", uiState = qbUiState)

        while(true) {
            sendRequest(command) {
                command  = when (it) {
                    is QuickButtonSelected -> {
                        when (it.metadata) {
                            QuickButton.ACTION.name -> buildSendMessage("Выбрана кнопка:" +
                                    " ${QuickButton.ACTION.title}. Здесь может быть любая обработка")
                            QuickButton.FORM.name -> buildSendUiState(formUiState)
                            else -> throw Exception("Unexpected type message")
                        }
                    }
                    is FormMessageSent -> buildSendUiState(qbUiState)
                    is FormClosed -> buildSendUiState(qbUiState)
                    is FormSubmitted -> buildSendMessage("Полученная метаинформация: ${it.metadata}")
                    else -> throw Exception("Unexpected type message")
                }
            }
        }
    }
}
```

