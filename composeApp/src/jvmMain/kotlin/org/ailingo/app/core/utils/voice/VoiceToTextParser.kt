package org.ailingo.app.core.utils.voice

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//TODO FOR DESKTOP
actual class VoiceToTextParser {
    private val _voiceState = MutableStateFlow(VoiceStates())
    actual val voiceState = _voiceState.asStateFlow()

    actual fun startListening() {}

    actual fun stopListening() {
        _voiceState.value = _voiceState.value.copy(isSpeaking = false)
    }
}
