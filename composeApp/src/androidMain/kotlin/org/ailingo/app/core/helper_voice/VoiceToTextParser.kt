package org.ailingo.app.core.helper_voice

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.ailingo.app.AndroidApp

actual class VoiceToTextParser : RecognitionListener {
    private val application: Application
        get() {
            return AndroidApp.INSTANCE
        }

    private val _voiceState = MutableStateFlow(VoiceStates())
    actual val voiceState = _voiceState.asStateFlow()

    private val recognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(application)

    actual fun startListening() {
        _voiceState.update {
            VoiceStates(isSpeaking = true)
        }
        if (!SpeechRecognizer.isRecognitionAvailable(application)) {
            _voiceState.update {
                it.copy(error = "Recognition is not available")
            }
            return
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru")
        }
        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)
    }

    actual fun stopListening() {
        _voiceState.update {
            it.copy(isSpeaking = false)
        }
        recognizer.stopListening()
    }

    override fun onReadyForSpeech(params: Bundle?) {
        _voiceState.update {
            it.copy(error = null)
        }
    }

    override fun onBeginningOfSpeech() {
        _voiceState.update {
            it.copy(isSpeaking = true)
        }
    }

    override fun onRmsChanged(rmsdB: Float) = Unit

    override fun onBufferReceived(buffer: ByteArray?) = Unit

    override fun onEndOfSpeech() {
        _voiceState.update {
            it.copy(isSpeaking = false)
        }
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT) {
            return
        }
        _voiceState.update {
            it.copy(error = "Error: $error")
        }
    }

    override fun onResults(results: Bundle?) {
        results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.getOrNull(0)?.let { result ->
            _voiceState.update {
                it.copy(spokenText = TextFieldValue(result))
            }
        }
    }

    override fun onPartialResults(partialResults: Bundle?) = Unit

    override fun onEvent(eventType: Int, params: Bundle?) = Unit

}