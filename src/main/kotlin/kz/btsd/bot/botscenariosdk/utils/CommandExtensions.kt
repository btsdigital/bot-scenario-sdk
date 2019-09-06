package kz.btsd.bot.botscenariosdk.utils

import kz.btsd.messenger.bot.api.model.command.Command
import kz.btsd.messenger.bot.api.model.command.EditMessage
import kz.btsd.messenger.bot.api.model.command.SendContactMessage
import kz.btsd.messenger.bot.api.model.command.SendContainerMessage
import kz.btsd.messenger.bot.api.model.command.SendMessage
import kz.btsd.messenger.bot.api.model.command.SendUiState

val Command.messengerId
    get() = when (this) {
        is SendMessage -> recipient.id
        is EditMessage -> recipient.id
        is SendContactMessage -> recipient.id
        is SendUiState -> recipient.id
        is SendContainerMessage -> recipient.id
        else -> throw IllegalArgumentException("Unsupported command type")
    }
