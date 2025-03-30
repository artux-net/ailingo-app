package org.ailingo.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.setSingletonImageLoaderFactory
import org.ailingo.app.core.utils.coil.asyncImageLoader
import org.ailingo.app.core.utils.coil.enableDiskCache
import org.ailingo.app.core.utils.voice.VoiceToTextParser
import org.ailingo.app.core.utils.windowinfo.util.PlatformName
import org.koin.compose.KoinContext

@Composable
internal fun App(
    voiceToTextParser: VoiceToTextParser,
    navController: NavHostController = rememberNavController(),
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
            navController = navController
        )
    }
}

internal expect fun openUrl(url: String?)
internal expect fun getPlatformName(): PlatformName
internal expect fun playSound(sound: String)

@Composable
internal expect fun getConfiguration(): Pair<Int, Int>