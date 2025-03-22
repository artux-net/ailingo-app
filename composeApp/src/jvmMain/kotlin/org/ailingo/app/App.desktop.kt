package org.ailingo.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import javazoom.jl.player.advanced.AdvancedPlayer
import javazoom.jl.player.advanced.PlaybackEvent
import javazoom.jl.player.advanced.PlaybackListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ailingo.app.core.utils.voice.VoiceStates
import org.ailingo.app.core.utils.windowinfo.util.PlatformName
import java.awt.Desktop
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URI
import java.net.URL
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import kotlin.math.sqrt

internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
}

fun recordAudio(
    voiceState: MutableStateFlow<VoiceStates>
): ByteArray {
    val audioFormat = AudioFormat(44100f, 16, 1, true, false)

    val targetDataLineInfo = DataLine.Info(TargetDataLine::class.java, audioFormat)
    if (!AudioSystem.isLineSupported(targetDataLineInfo)) {
        return ByteArray(0)
    }

    val targetDataLine = AudioSystem.getLine(targetDataLineInfo) as TargetDataLine
    targetDataLine.open(audioFormat)
    targetDataLine.start()

    val audioBuffer = ByteArray(4096)
    val byteArrayOutputStream = ByteArrayOutputStream()

    // Start recording
    targetDataLine.start()

    // Variable to keep track of silence counter
    var silenceCounter = 0

    // How much silence is needed before ending the recording (adjust as needed)
    val maxSilenceCounter = 50 // For example, 40 empty packets to end the recording

    while (voiceState.value.isSpeaking) {
        val bytesRead = targetDataLine.read(audioBuffer, 0, audioBuffer.size)

        // Analyze sound level in the current audio buffer
        val volume = calculateVolume(audioBuffer, bytesRead)

        if (volume > 0.02) { // Threshold value
            silenceCounter = 0 // Reset silence counter
        } else {
            silenceCounter++
        }

        if (silenceCounter >= maxSilenceCounter) {
            // Reached maximum silence count, stop recording
            break
        }

        byteArrayOutputStream.write(audioBuffer, 0, bytesRead)
    }

    voiceState.update {
        VoiceStates(spokenText = "Wait for transcripts...")
    }

    byteArrayOutputStream.close()
    targetDataLine.stop()
    targetDataLine.close()

    return byteArrayOutputStream.toByteArray()
}

fun calculateVolume(audioBuffer: ByteArray, bytesRead: Int): Double {
    var sum = 0.0
    for (i in 0 until bytesRead / 2) {
        val sample =
            (audioBuffer[2 * i].toInt() or (audioBuffer[2 * i + 1].toInt() shl 8)) / 32768.0
        sum += sample * sample
    }

    return sqrt(sum / (bytesRead / 2))
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
            player?.playBackListener = object : PlaybackListener() {
                override fun playbackFinished(evt: PlaybackEvent?) {
                    super.playbackFinished(evt)
                    player?.close()
                }
            }
            player?.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun getConfiguration(): Pair<Int, Int> {
    val containerSize = LocalWindowInfo.current.containerSize
    return Pair(containerSize.width, containerSize.height)
}