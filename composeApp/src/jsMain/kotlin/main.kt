import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.ailingo.app.App
import org.ailingo.app.core.utils.voice.VoiceToTextParser
import org.ailingo.app.di.initKoin
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    onWasmReady {
        val body = document.body ?: return@onWasmReady
        ComposeViewport(body) {
            val voiceToTextParser by lazy {
                VoiceToTextParser()
            }
            App(
                voiceToTextParser
            )
        }
    }
}
