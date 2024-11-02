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
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.ailingo.app.feature_topics.data.Topic
import org.ailingo.app.feature_topics.presentation.TopicCard
import org.ailingo.app.feature_upload_avatar.UploadAvatarViewModel
import org.ailingo.composeApp.database.HistoryDictionaryDatabase
import org.w3c.dom.Audio
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Worker
import org.w3c.files.File
import org.w3c.files.FileReader
import org.w3c.files.get
import kotlin.js.Promise

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

actual fun getPlatformName(): String {
    return "Web"
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
    val containerSize = LocalWindowInfo.current.containerSize
    return Pair(containerSize.width, containerSize.height)
}

actual class DriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return WebWorkerDriver(
            Worker(
                js("""new URL("sqlite.worker.js", import.meta.url)""")
            )
        ).also { HistoryDictionaryDatabase.Schema.create(it).await() }
    }
}

actual suspend fun selectImageWebAndDesktop(): String? {

    val input = document.createElement("input") as HTMLInputElement
    input.type = "file"

    // Set accept attribute to limit file selection to images
    input.accept = "image/jpg"
    input.accept = "image/png"

    // Use Promise to handle asynchronous file reading
    val promise = Promise<String?> { resolve, _ ->
        input.onchange = { _ ->
            val file = input.files?.get(0)
            if (file != null) {
                readAsBase64(file) { base64String ->
                    resolve(base64String)
                }
            } else {
                resolve(null)
            }
        }
    }

    // Simulate a click on the input element to trigger file selection
    input.click()

    // Return the result as a nullable base64 string
    return promise.await()
}

private fun readAsBase64(file: File, callback: (String?) -> Unit) {
    val reader = FileReader()

    reader.onload = { _ ->
        val result = reader.result as? String
        val base64String = result?.substringAfter("base64,")
        callback(base64String)
    }

    reader.onerror = { _ ->
        callback(null)
    }

    // Read the file as a data URL (base64)
    reader.readAsDataURL(file)
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
    uploadAvatarViewModel: UploadAvatarViewModel,
    login: String,
    password: String,
    email: String,
    name: String,
    onNavigateToRegisterScreen: () -> Unit
) {
}