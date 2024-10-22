package org.ailingo.app.core.helper_voice

import androidx.compose.ui.text.input.TextFieldValue

data class VoiceStates(
    var spokenText: TextFieldValue = TextFieldValue(""),
    val isSpeaking: Boolean = false,
    val error: String? = null
)