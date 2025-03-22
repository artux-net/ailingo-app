package org.ailingo.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import kotlinx.browser.window
import org.ailingo.app.core.utils.windowinfo.util.PlatformName
import org.w3c.dom.Audio

internal actual fun openUrl(url: String?) {
    url?.let { window.open(it) }
}

@JsModule("./voiceToText.js")
@JsNonModule
external object VoiceToText {
    fun startListening()
    fun stopListening()
    fun setRecognitionCallback(callback: (String) -> Unit)
    fun setListeningCallback(callback: (Boolean) -> Unit)
}

actual fun getPlatformName(): PlatformName {
    return PlatformName.Web
}

actual fun playSound(sound: String) {
    if (sound == "") return
    val audio = Audio(sound)
    val playPromise = audio.play()
    if (playPromise !== undefined) {
        playPromise.catch { error ->
            console.log(error)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun getConfiguration(): Pair<Int, Int> {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current.density
    val width = (windowInfo.containerSize.width / density)
    val height = (windowInfo.containerSize.height / density)
    return Pair(width.toInt(), height.toInt())
}
