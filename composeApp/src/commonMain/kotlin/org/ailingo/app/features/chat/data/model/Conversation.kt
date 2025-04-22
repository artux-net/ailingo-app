package org.ailingo.app.features.chat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: String,
    val conversationId: String,
    val content: String,
    val timestamp: String,
    val type: String,
    val suggestions: List<String>? = emptyList()
)