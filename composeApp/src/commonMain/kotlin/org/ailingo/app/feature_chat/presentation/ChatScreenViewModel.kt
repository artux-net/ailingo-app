package org.ailingo.app.feature_chat.presentation

import AiLingo.composeApp.BuildConfig.BASE_URL
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.ailingo.app.core.helper_auth.auth.basicAuthHeader
import org.ailingo.app.feature_chat.data.model.Message

class ChatScreenViewModel: ViewModel() {
    private val _chatState = mutableStateListOf<Message>()
    val chatState: List<Message> = _chatState

    private val _isActiveJob = MutableSharedFlow<Boolean>()
    val isActiveJob = _isActiveJob.asSharedFlow()

    init {
        _chatState.add(
            Message("Привет, как я могу вам помочь?", false)
        )
    }

    private val API_ENDPOINT = "/api/v1/chat/message"
    private val USERNAME = "admin"
    private val PASSWORD = "pass"

    fun onEvent(event: ChatScreenEvents) {
        when (event) {
            is ChatScreenEvents.MessageSent -> {
                _chatState.add(Message(event.message, isSentByUser = true))
                _chatState.add(Message("Waiting for response...", isSentByUser = false))
                viewModelScope.launch {
                    _isActiveJob.emit(true)
                    val localHttpClient = HttpClient {
                        install(ContentNegotiation) {
                            json(Json {
                                ignoreUnknownKeys = true
                            })
                        }
                    }
                    try {
                        val response = localHttpClient.post("$BASE_URL$API_ENDPOINT") {
                            header(HttpHeaders.Authorization, basicAuthHeader(USERNAME, PASSWORD))
                            header(HttpHeaders.ContentType, ContentType.Application.Json)
                            setBody(event.message)
                        }
                        when {
                            response.status.isSuccess() -> {
                                val responseBody = response.body<String>()
                                _chatState.removeAt(_chatState.size - 1)
                                _chatState.add(Message(responseBody, isSentByUser = false))
                                _isActiveJob.emit(false)
                            } else -> {
                            _chatState.removeAt(_chatState.size - 1)
                            _chatState.add(
                                Message(
                                    "Request failed with ${response.status}",
                                    isSentByUser = false
                                )
                            )
                            _isActiveJob.emit(false)
                        }
                        }
                    } catch (e: Exception) {
                        _chatState.removeAt(_chatState.size - 1)
                        _chatState.add(Message("Exception: ${e.message.toString()}", isSentByUser = false))
                        _isActiveJob.emit(false)
                    } finally {
                        localHttpClient.close()
                    }
                }
            }
        }
    }
}