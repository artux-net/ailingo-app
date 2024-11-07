package org.ailingo.app.core.helper.voice

data class VoiceStates(
    var spokenText: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null
)
