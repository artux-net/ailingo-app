package org.ailingo.app.features.chat.presentation

sealed class ChatScreenEvents {
    data class MessageSent(val message: String) : ChatScreenEvents()
}
