import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.intertight_black
import ailingo.composeapp.generated.resources.intertight_bold
import ailingo.composeapp.generated.resources.intertight_extra_bold
import ailingo.composeapp.generated.resources.intertight_extra_light
import ailingo.composeapp.generated.resources.intertight_light
import ailingo.composeapp.generated.resources.intertight_medium
import ailingo.composeapp.generated.resources.intertight_regular
import ailingo.composeapp.generated.resources.intertight_semibold
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.ailingo.app.App
import org.ailingo.app.core.utils.voice.VoiceToTextParser
import org.ailingo.app.di.initKoin
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class, ExperimentalResourceApi::class)
fun main() {
    initKoin()
    onWasmReady {
        val body = document.body ?: return@onWasmReady
        ComposeViewport(body) {
            val voiceToTextParser by lazy {
                VoiceToTextParser()
            }

            val fontExtraLight by preloadFont(Res.font.intertight_extra_light)
            val fontLight by preloadFont(Res.font.intertight_light)
            val fontRegular by preloadFont(Res.font.intertight_regular)
            val fontMedium by preloadFont(Res.font.intertight_medium)
            val fontSemiBold by preloadFont(Res.font.intertight_semibold)
            val fontBold by preloadFont(Res.font.intertight_bold)
            val fontExtraBold by preloadFont(Res.font.intertight_extra_bold)
            val fontBlack by preloadFont(Res.font.intertight_black)

            val fontsReady = remember(
                fontExtraLight, fontLight, fontRegular, fontMedium,
                fontSemiBold, fontBold, fontExtraBold, fontBlack
            ) {
                fontExtraLight != null &&
                        fontLight != null &&
                        fontRegular != null &&
                        fontMedium != null &&
                        fontSemiBold != null &&
                        fontBold != null &&
                        fontExtraBold != null &&
                        fontBlack != null
            }

            if (fontsReady) {
                App(
                    voiceToTextParser
                )
            } else {
                Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.8f))) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}