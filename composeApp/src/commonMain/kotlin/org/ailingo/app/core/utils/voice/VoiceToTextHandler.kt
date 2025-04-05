package org.ailingo.app.core.utils.voice

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow

sealed class VoiceToTextState {
    data object Idle : VoiceToTextState()
    data object Listening : VoiceToTextState()
    data class Error(val message: String) : VoiceToTextState()
    data class Result(val text: String) : VoiceToTextState()
}

expect class VoiceToTextHandler {
    val state: StateFlow<VoiceToTextState>
    var isAvailable: Boolean

    fun startListening(languageCode: String = "en-US")
    fun stopListening()
}

@Composable
expect fun rememberVoiceToTextHandler(): VoiceToTextHandler