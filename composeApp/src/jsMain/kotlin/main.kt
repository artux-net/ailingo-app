import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.browser.document
import org.ailingo.app.App
import org.ailingo.app.RootComponent
import org.ailingo.app.core.helper_voice.VoiceToTextParser
import org.ailingo.app.feature_dictionary_history.di.AppModule
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

            val lifecycle = LifecycleRegistry()

            val root = RootComponent(
                    componentContext = DefaultComponentContext(lifecycle = lifecycle),
                    historyDictionaryRepository = appModule.dictionaryRepository,
            )

            App(
                voiceToTextParser,
                root
            )
        }
    }
}