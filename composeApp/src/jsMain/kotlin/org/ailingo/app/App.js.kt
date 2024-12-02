package org.ailingo.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ailingo.app.core.utils.windowinfo.util.PlatformName
import org.ailingo.app.database.HistoryDictionaryDatabase
import org.ailingo.app.features.registration.presentation.uploadavatar.UploadAvatarViewModel
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

actual class DriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return WebWorkerDriver(
            Worker(
                js("""new URL("sqlite.worker.js", import.meta.url)""")
            )
        ).also { HistoryDictionaryDatabase.Schema.create(it).await() }
    }
}

actual fun selectImageWebAndDesktop(scope: CoroutineScope, callback: (String?) -> Unit) {
    scope.launch {
        val file = withContext(Dispatchers.Default) {
            // Create a hidden input element
            val input = document.createElement("input") as HTMLInputElement
            input.type = "file"
            input.accept = "image/*" // Accept only image files
            input.style.display = "none"
            document.body?.appendChild(input)

            // Wait for the user to select a file
            var selectedFile: File? = null
            val promise = Promise { resolve, reject ->
                input.onchange = {
                    selectedFile = input.files?.get(0)
                    resolve(selectedFile)
                }
                input.click() // Simulate a click to open the file picker
            }
            promise.await()
            input.remove()
            selectedFile
        }
        val base64String = if (file == null) null else encodeFileToBase64(file)
        withContext(Dispatchers.Main) {
            callback(base64String)
        }
    }
}

private suspend fun encodeFileToBase64(file: File): String? = withContext(Dispatchers.Default){
    val reader = FileReader()
    val promise = Promise<String> { resolve, reject ->
        reader.onload = {
            resolve((reader.result as String).substringAfter(","))
        }
        reader.onerror = { event ->
            console.error("FileReader error: ", event)
            reject(Exception("FileReader error: $event"))
        }
        reader.readAsDataURL(file)
    }
    try {
        promise.await()
    } catch (e: Throwable) {
        console.error("Error encoding file to base64: ", e)
        return@withContext null
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