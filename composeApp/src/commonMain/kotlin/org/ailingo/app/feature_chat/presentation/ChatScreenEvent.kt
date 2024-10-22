package org.ailingo.app.feature_chat.presentation

sealed class ChatScreenEvents {
    data class MessageSent(val message: String) : ChatScreenEvents()
}