package org.ailingo.app

import javazoom.jl.player.advanced.AdvancedPlayer
import javazoom.jl.player.advanced.PlaybackEvent
import javazoom.jl.player.advanced.PlaybackListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.ailingo.app.core.utils.deviceinfo.util.PlatformName
import java.awt.Desktop
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URI
import java.net.URL

internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
}

internal actual fun getPlatformName(): PlatformName {
    return PlatformName.Desktop
}

@OptIn(DelicateCoroutinesApi::class)
internal actual fun playSound(sound: String) {
    var player: AdvancedPlayer?
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val url = URL(sound)
            val inputStream: InputStream = BufferedInputStream(url.openStream())
            player = AdvancedPlayer(inputStream)
            player.playBackListener = object : PlaybackListener() {
                override fun playbackFinished(evt: PlaybackEvent?) {
                    super.playbackFinished(evt)
                    player.close()
                }
            }
            player.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}