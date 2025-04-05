package org.ailingo.app

import kotlinx.browser.window
import org.ailingo.app.core.utils.deviceinfo.util.PlatformName

internal actual fun openUrl(url: String?) {
    url?.let { window.open(it) }
}

actual fun getPlatformName(): PlatformName {
    return PlatformName.Web
}

actual fun playSound(sound: String) {
//    if (sound == "") return
//    val audio = Audio(sound)
//    val playPromise = audio.play()
//    if (playPromise !== undefined) {
//        playPromise.catch { error ->
//            console.log(error)
//        }
//    }
}