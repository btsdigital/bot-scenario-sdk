package kz.btsd.bot.botscenariosdk.utils

import kz.btsd.messenger.bot.api.model.update.FormClosed
import kz.btsd.messenger.bot.api.model.update.FormMessageSent
import kz.btsd.messenger.bot.api.model.update.FormSubmitted
import kz.btsd.messenger.bot.api.model.update.FormUpdate
import kz.btsd.messenger.bot.api.model.update.InlineCommandSelected
import kz.btsd.messenger.bot.api.model.update.InterfaceViewSelected
import kz.btsd.messenger.bot.api.model.update.InvitedToChannel
import kz.btsd.messenger.bot.api.model.update.InvitedToGroup
import kz.btsd.messenger.bot.api.model.update.KickedFromChannel
import kz.btsd.messenger.bot.api.model.update.KickedFromGroup
import kz.btsd.messenger.bot.api.model.update.Message
import kz.btsd.messenger.bot.api.model.update.MessageEdited
import kz.btsd.messenger.bot.api.model.update.MessageIdAssigned
import kz.btsd.messenger.bot.api.model.update.NewChannelSubscriber
import kz.btsd.messenger.bot.api.model.update.QuickButtonSelected
import kz.btsd.messenger.bot.api.model.update.Update


val Update.messengerId
    get() = when (this) {
        is Message -> dialog.id
        is InlineCommandSelected -> dialog.id
        is QuickButtonSelected -> dialog.id
        is FormUpdate -> dialog.id
        is InterfaceViewSelected -> dialog.id
        is NewChannelSubscriber -> channelId
        is FormClosed -> dialog.id
        is FormMessageSent -> dialog.id
        is FormSubmitted -> dialog.id
        is InvitedToGroup -> groupId
        is InvitedToChannel -> channelId
        is KickedFromChannel -> channelId
        is KickedFromGroup -> groupId
        is MessageEdited -> dialog.id
        is MessageIdAssigned -> dialog.id
        else -> throw IllegalArgumentException("Unsupported update type")
    }
