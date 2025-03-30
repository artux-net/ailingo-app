package org.ailingo.app.features.chat.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.chat.data.model.Conversation
import org.ailingo.app.features.chat.domain.repository.ChatRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatViewModel(
    private val chatRepository: ChatRepository,
    topicName: String
) : ViewModel() {
    private val _chatState = MutableStateFlow<UiState<MutableList<Conversation>>>(UiState.Idle())
    val chatState = _chatState.asStateFlow()
    private var conversationId by mutableStateOf("")

    private val _messages = MutableStateFlow<List<Conversation>>(mutableListOf())
    val messages = _messages.asStateFlow()

    init {
        onEvent(ChatEvents.OnStartConversation(topicName))
    }

    fun onEvent(event: ChatEvents) {
        when (event) {
            is ChatEvents.OnStartConversation -> {
                startConversation(event.topicName)
            }

            is ChatEvents.OnSendMessage -> {
                sendMessage(conversationId = conversationId, message = event.message)
            }
        }
    }

    private fun startConversation(topicName: String) {
        _chatState.value = UiState.Loading()
        viewModelScope.launch {
            chatRepository.startChat(topicName).collect { state ->
                when (state) {
                    is UiState.Success -> {
                        conversationId = state.data.conversationId
                        _messages.value = mutableListOf(state.data)
                        _chatState.value = UiState.Success(mutableListOf(state.data))
                    }

                    is UiState.Error -> {
                        _chatState.value = UiState.Error(state.message)
                    }

                    is UiState.Loading -> {
                        _chatState.value = UiState.Loading()
                    }

                    is UiState.Idle -> {
                        _chatState.value = UiState.Idle()
                    }
                }
            }
        }
    }

    private fun sendMessage(conversationId: String, message: String) {
        if (message.isBlank()) return
        val userMessage = Conversation(
            id = generateUniqueId(),
            conversationId = conversationId,
            content = message,
            timestamp = getCurrentTimestamp(),
            type = "USER"
        )

        val currentMessages = _messages.value.toMutableList()
        currentMessages.add(userMessage)
        _messages.value = currentMessages

        _chatState.value = UiState.Loading()
        viewModelScope.launch {
            chatRepository.sendMessage(conversationId, message).collect { state ->
                when (state) {
                    is UiState.Success -> {
                        val updatedMessages = _messages.value.toMutableList()
                        updatedMessages.add(state.data)
                        _messages.value = updatedMessages
                        _chatState.value = UiState.Success(_messages.value.toMutableList())
                    }

                    is UiState.Error -> {
                        _chatState.value = UiState.Error(state.message)
                    }

                    is UiState.Loading -> {
                        _chatState.value = UiState.Loading()
                    }

                    is UiState.Idle -> {
                        _chatState.value = UiState.Idle()
                    }
                }
            }
        }
    }


    @Suppress("MemberExtensionConflict")
    @OptIn(ExperimentalUuidApi::class)
    private fun generateUniqueId(): String {
        return Uuid.random().toString()
    }

    private fun getCurrentTimestamp(): String {
        val currentInstant: Instant = Clock.System.now()
        val timeZone: TimeZone = TimeZone.UTC
        val localDateTime = currentInstant.toLocalDateTime(timeZone)
        return localDateTime.toString() + "Z"
    }
}