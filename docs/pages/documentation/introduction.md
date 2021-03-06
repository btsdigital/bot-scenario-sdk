---
title: Введение
sidebar: home_sidebar
keywords: introduction, about
permalink: introduction.html
toc: true
folder: documentation
---
## Общие сведения

Это руководство станет для вас экскурсом в мир [сервисов](TODO!#сервис) приложения [мессенджер](TODO#Мессенджер). В ходе чтения вы узнаете о том, как [сервисы](TODO!#сервис) работают и как они создаются.

[Сервисы](TODO!#сервис) были популярной темой в течение определённого времени, и много людей сегодня увлечены ими. Некоторые даже верят, что за подобными [сервисами](TODO!#сервис) будущее и рано или поздно они заменят приложения. Однако находятся и те люди, которые считают, что [сервисы](TODO!#сервис), как идея, обречены на провал.

В данном руководстве, вместо разговоров о будущем [сервисов](TODO!#сервис), мы проведём вам краткий экскурс в тему [сервисов](TODO!#сервис), того, как они работают и как создать такие самостоятельно, использую наш SDK.



[Сервисы](TODO!#сервис) — специальные аккаунты в [мессенджер](TODO#Мессенджер), созданные для того, чтобы автоматически обрабатывать и отправлять сообщения. Пользователи могут взаимодействовать с [сервисами](TODO!#сервис) при помощи сообщений, отправляемых через обычные или групповые чаты, а таже в каналах. Логика бота контролируется при помощи HTTPS запросов к нашему [API для сервисов](TODO!BOTAPI).




## Как это работает

![Chatbot diagram](images/chat bot diagram.jpg "Chatbot diagram")


Прежде всего, [сервис](TODO!#сервис) для [мессенджер](TODO#Мессенджер) — это по-прежнему приложение, запущенное на вашей стороне и осуществляющее запросы к [Bot API](TODO!BOTAPI) приложения [мессенджер](TODO#Мессенджер) используя <a href="https://ru.wikipedia.org/wiki/REST" target="_blank">REST</a> архитектуру. Причем API довольно простое — [сервис](TODO!#сервис) обращается на определенный URL с параметрами, а [мессенджер](TODO#Мессенджер) отвечает <a href="https://www.json.org/" target="_blank">JSON</a> объектом.