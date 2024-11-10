import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.ailingo.app.App
import org.ailingo.app.core.utils.voice.VoiceToTextParser
import org.ailingo.app.features.dictionary.history.di.AppModule
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        val body = document.body ?: return@onWasmReady
        ComposeViewport(body) {
            val voiceToTextParser by lazy {
                VoiceToTextParser()
            }

            val appModule = AppModule()

            App(
                voiceToTextParser,
                appModule.dictionaryRepository
            )
        }
    }
}
