package org.ailingo.app

import android.content.Intent
import android.media.MediaPlayer
import androidx.core.net.toUri
import org.ailingo.app.core.utils.deviceinfo.util.PlatformName

internal actual fun openUrl(url: String?) {
    val uri = url?.toUri() ?: return
    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = uri
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    AndroidApp.INSTANCE.startActivity(intent)
}

internal actual fun getPlatformName(): PlatformName {
    return PlatformName.Android
}

actual fun playSound(sound: String) {
    val mediaPlayer = MediaPlayer()
    mediaPlayer.setDataSource(sound)
    mediaPlayer.prepare()
    mediaPlayer.start()
}