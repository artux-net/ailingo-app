package org.ailingo.app

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.ailingo.app.core.helper_voice.VoiceToTextParser
import org.ailingo.app.feature_dictionary_history.di.AppModule
import javax.swing.SwingUtilities

fun main() = application {
    Window(
        title = "ailingo",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        val voiceToTextParser by lazy {
            VoiceToTextParser()
        }
        val lifecycle = LifecycleRegistry()

        // Always create the root component outside Compose on the UI thread
        val root =
            runOnUiThread {
                RootComponent(
                    componentContext = DefaultComponentContext(lifecycle = lifecycle),
                    AppModule().dictionaryRepository
                )
            }

        App(voiceToTextParser = voiceToTextParser, root = root)
    }
}

internal fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    SwingUtilities.invokeAndWait {
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }

    error?.also { throw it }

    @Suppress("UNCHECKED_CAST")
    return result as T
}