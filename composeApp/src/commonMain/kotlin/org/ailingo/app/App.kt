package org.ailingo.app

import androidx.compose.runtime.Composable
import coil3.compose.setSingletonImageLoaderFactory
import org.ailingo.app.core.utils.coil.asyncImageLoader
import org.ailingo.app.core.utils.coil.enableDiskCache
import org.ailingo.app.core.utils.voice.VoiceToTextParser
import org.ailingo.app.core.utils.windowinfo.util.PlatformName
import org.koin.compose.KoinContext

@Composable
internal fun App(
    voiceToTextParser: VoiceToTextParser,
) {
    KoinContext {
        setSingletonImageLoaderFactory { context ->
            if (getPlatformName() == PlatformName.Web) {
                context.asyncImageLoader()
            } else {
                context.asyncImageLoader().enableDiskCache()
            }
        }
        AiLingoNavGraph(
            voiceToTextParser = voiceToTextParser,
        )
    }
}

internal expect fun openUrl(url: String?)
internal expect fun getPlatformName(): PlatformName
internal expect fun playSound(sound: String)

@Composable
internal expect fun getConfiguration(): Pair<Int, Int>