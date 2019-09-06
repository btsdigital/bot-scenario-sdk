---
title: Конфигурация
sidebar: home_sidebar
keywords: configuration
permalink: configuration.html
toc: true
folder: documentation
---
# Конфигурация
Библиотека предоставляет возможность конфигурации следующих свойств:
 - sdk.scenario.defaultErrorMessage
 - sdk.scenario.defaultRequestEnumErrorMessage

```kotlin
sdk.scenario.defaultErrorMessage: "Сообщение не может быть прочитано. Воспользуйтесь меню данного сервиса для вызова нужной функции или убедитесь, что отправляете понятные для данного сервиса команды"
sdk.scenario.defaultRequestEnumErrorMessage: "Выберите из предложенных вариантов"
```

## defaultErrorMessage
Текст ошибки по умолчанию для метода `sendRequest`. Дефолтное значение `incorrect input, try again`
Выдается в случае ошибки в блоке `responseExtractor` метода `sendRequest`, если явно не указано свойство `validationErrorMessage`
```kotlin
sendRequest("message") { throw RuntimeException("demo exception")
```
В методе `sendRequest` не указан параметр `validationErrorMessage`, поэтому в результате ошибки в блоке `responseExtractor`
будет выведено сообщение по умолчанию, содержащееся в конфигурации с ключом `sdk.scenario.defaultErrorMessage`

## defaultRequestEnumErrorMessage
Текст ошибки по умолчанию для метода `sendEnumRequest`. Дефолтное значение `incorrect input, select option`
Выдается, когда пользователь вместо выбора из предложенных вариантов осуществляет ввод информации и если явно не указано свойство `validationErrorMessage`
```kotlin
sendEnumRequest<OkOption>("message") { it.title }
```
В методе `sendEnumRequest` не указан параметр `validationErrorMessage`, поэтому в результате некорректного ввода
будет выведено сообщение по умолчанию, содержащееся в конфигурации с ключом `sdk.scenario.defaultRequestEnumErrorMessage`

## Пример использования
Установите значение параметра `sdk.scenario.defaultErrorMessage`
```kotlin
sdk.scenario.defaultErrorMessage: "Сообщение не может быть прочитано. Воспользуйтесь меню данного сервиса для вызова нужной функции или убедитесь, что отправляете понятные для данного сервиса команды"
```

<img src="images/defaultErrorSpecify.png" height="50%">

