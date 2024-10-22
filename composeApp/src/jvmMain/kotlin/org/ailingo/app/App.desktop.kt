package org.ailingo.app

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import javazoom.jl.player.advanced.AdvancedPlayer
import javazoom.jl.player.advanced.PlaybackEvent
import javazoom.jl.player.advanced.PlaybackListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ailingo.app.core.helper_voice.VoiceStates
import org.ailingo.app.feature_topics.data.Topic
import org.ailingo.app.feature_topics.presentation.TopicCard
import org.ailingo.app.feature_upload_avatar.UploadAvatarComponent
import org.ailingo.composeApp.database.HistoryDictionaryDatabase
import java.awt.Desktop
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.util.Base64
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
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
        VoiceStates(spokenText = TextFieldValue("Wait for transcripts..."))
    }

    byteArrayOutputStream.close()
    targetDataLine.stop()
    targetDataLine.close()

    return byteArrayOutputStream.toByteArray()
}

// Function to calculate sound level in the buffer
fun calculateVolume(audioBuffer: ByteArray, bytesRead: Int): Double {
    var sum = 0.0
    for (i in 0 until bytesRead / 2) {
        val sample =
            (audioBuffer[2 * i].toInt() or (audioBuffer[2 * i + 1].toInt() shl 8)) / 32768.0
        sum += sample * sample
    }

    return sqrt(sum / (bytesRead / 2))
}

internal actual fun getPlatformName(): String {
    return "Desktop"
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

actual class DriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        val driver =
            JdbcSqliteDriver(url = "jdbc:sqlite:dictionary_database.db")
                .also {
                    HistoryDictionaryDatabase.Schema.create(driver = it).await()
                }
        return driver

    }
}

@Suppress("NewApi")
actual suspend fun selectImageWebAndDesktop(): String? {
    val fileChooser = JFileChooser()

    // Создание фильтра для файлов PNG и JPG
    val filter = FileNameExtensionFilter("Image files", "png", "jpg")
    fileChooser.fileFilter = filter

    // Отображение диалогового окна выбора файла
    fileChooser.showOpenDialog(null)

    val selectedFile = fileChooser.selectedFile

    // Проверка, что файл был действительно выбран
    return if (selectedFile == null) {
        null
    } else {
        encodeFileToBase64(selectedFile)
    }
}

@Suppress("NewApi")
fun encodeFileToBase64(file: java.io.File): String {
    val fileContent = Files.readAllBytes(file.toPath())
    return Base64.getEncoder().encodeToString(fileContent)
}

@Composable
actual fun TopicsForDesktopAndWeb(topics: List<Topic>) {
    val horizontalScrollbarStyle = ScrollbarStyle(
        minimalHeight = 0.dp,
        thickness = 12.dp,
        shape = RectangleShape,
        hoverDurationMillis = 100,
        unhoverColor = Color.Gray,
        hoverColor = Color.DarkGray
    )
    val horizontalScrollState = rememberScrollState(0)
    val topicsCount = topics.size
    val topicsPerColumn = 2
    val columnsCount = topicsCount / topicsPerColumn
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxSize().horizontalScroll(horizontalScrollState)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(columnsCount) { columnIndex ->
                Column(modifier = Modifier.fillMaxHeight()) {
                    repeat(topicsPerColumn) { rowIndex ->
                        val topicIndex = columnIndex * topicsPerColumn + rowIndex
                        TopicCard(
                            topics[topicIndex],
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        HorizontalScrollbar(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(),
            adapter = rememberScrollbarAdapter(horizontalScrollState),
            style = horizontalScrollbarStyle
        )
    }
}

@Composable
actual fun UploadAvatarForPhone(
    uploadAvatarComponent: UploadAvatarComponent,
    login: String,
    password: String,
    email: String,
    name: String,
    onNavigateToRegisterScreen: () -> Unit
) {
}