package org.ailingo.app

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.ailingo.app.core.helper_voice.VoiceToTextParser
import org.ailingo.app.feature_dictionary_history.di.AppModule

fun main() = application {
    Window(
        title = "ailingo",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        val voiceToTextParser by lazy {
            VoiceToTextParser()
        }

        App(voiceToTextParser = voiceToTextParser, AppModule().dictionaryRepository)
    }
}