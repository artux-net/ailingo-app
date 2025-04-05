package org.ailingo.app.core.utils.voice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

actual class VoiceToTextHandler {
    private val _state = MutableStateFlow<VoiceToTextState>(VoiceToTextState.Idle)
    actual val state: StateFlow<VoiceToTextState> = _state.asStateFlow()
    actual var isAvailable: Boolean = false

    init {
//        val recognitionClass = try {
//            window.asDynamic().webkitSpeechRecognition ?: window.asDynamic().SpeechRecognition
//        } catch (e: Throwable) {
//            null
//        }
//
//        isAvailable = recognitionClass != null
//        if (isAvailable) {
//            speechRecognition = try {
//                js("new webkitSpeechRecognition()")
//            } catch (e: Throwable) {
//                js("new SpeechRecognition()")
//            }
//            speechRecognition.continuous = true
//            speechRecognition.interimResults = true
//        }
    }

    actual fun startListening(languageCode: String) {

    }

    actual fun stopListening() {
        _state.value = VoiceToTextState.Idle
    }
}

@Composable
actual fun rememberVoiceToTextHandler(): VoiceToTextHandler {
    val voiceToTextHandler = remember { VoiceToTextHandler() }

    DisposableEffect(voiceToTextHandler) {
        onDispose {
            voiceToTextHandler.stopListening()
        }
    }

    return voiceToTextHandler
}