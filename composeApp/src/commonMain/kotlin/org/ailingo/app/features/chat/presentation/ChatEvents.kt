package org.ailingo.app.features.chat.presentation

sealed class ChatEvents {
    data class OnStartConversation(val topicName: String) : ChatEvents()
    data class OnSendMessage(val message: String) : ChatEvents()
}