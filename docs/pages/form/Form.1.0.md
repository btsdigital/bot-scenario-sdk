---
title: Формы 1.0
sidebar: home_sidebar
keywords: releases, release, 1.0.0
permalink: Form.1.0.html
toc: true
folder: forms
---
## Backdrop
```json
{
  "form": {
    "id": "form_id",
    "options": {
      "fullscreen": true
    },
    "header": {
      "type": "toolbar"
    },
    "content": [],
    "bottom_bar": {}
  }
}
```

| Поле  | Описание  |
|---|---|
| form.id  | Уникальный идентификатор формы  |
| form.options  |  Объект, который будет параметризировать форму |

> bottom_bar может быть только условии fullscreen: true, при fullscreen: false - bottom_bar не будет считываться


## Quick button