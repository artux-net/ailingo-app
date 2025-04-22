package org.ailingo.app.features.topics.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Topic(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val price: Int,
    val welcomePrompt: String,
    val systemPrompt: String,
    val messageLimit: Int,
    val isCompleted: Boolean,
    val topicXp: Int
)