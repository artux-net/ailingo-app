package org.ailingo.app.core.helper_voice

import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.ailingo.app.VoiceToText

actual class VoiceToTextParser {
    private val _voiceState = MutableStateFlow(VoiceStates())
    actual val voiceState = _voiceState.asStateFlow()

    init {
        VoiceToText.setRecognitionCallback { recognizedText ->
            _voiceState.value = _voiceState.value.copy(spokenText = TextFieldValue(recognizedText))
        }
        VoiceToText.setListeningCallback { isSpeaking ->
            _voiceState.value = _voiceState.value.copy(isSpeaking = isSpeaking)
        }
    }

    actual fun startListening() {
        VoiceToText.startListening()
    }

    actual fun stopListening() {
        VoiceToText.stopListening()
    }
}