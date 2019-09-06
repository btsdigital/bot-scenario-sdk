package kz.btsd.bot.botscenariosdk.utils.builder

import kz.btsd.messenger.bot.api.model.command.InlineCommand
import kz.btsd.messenger.bot.api.model.command.SendMessage
import kz.btsd.messenger.bot.api.model.command.SendUiState
import kz.btsd.messenger.bot.api.model.command.UiState
import kz.btsd.messenger.bot.api.model.media.InputMedia
import kz.btsd.messenger.bot.api.model.peer.PeerUser


fun buildSendMessage(
        messengerId: String,
        localId: String? = null,
        replyToMessageId: String? = null,
        content: String,
        inlineCommands: List<InlineCommand>? = null,
        uiState: UiState? = null,
        mediaList: List<InputMedia> = emptyList()
): SendMessage {
    return SendMessage(
            localId = localId,
            recipient = PeerUser(messengerId),
            replyToMessageId = replyToMessageId,
            content = content,
            inlineCommands = inlineCommands ?: emptyList(),
            uiState = uiState,
            mediaList = mediaList
    )
}

fun buildSendUiState(messengerId: String, uiState: UiState): SendUiState {
    return SendUiState(
            recipient = PeerUser(messengerId),
            uiState = uiState
    )
}

