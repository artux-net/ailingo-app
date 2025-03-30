package org.ailingo.app.features.profileupdate.presentation

import kotlinx.browser.document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList
import org.w3c.files.FileReader
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual suspend fun selectImage(): String? = withContext(Dispatchers.Default) {
    return@withContext chooseFileAndEncodeToBase64()
}

private suspend fun chooseFileAndEncodeToBase64(): String? = suspendCancellableCoroutine { continuation ->
    val input = document.createElement("input") as HTMLInputElement
    input.type = "file"
    input.accept = "image/*" // Optional: filter for image files
    input.style.display = "none" // Hide the input element

    input.addEventListener("change", { event ->
        val files = input.files?.asList()
        if (files != null && files.isNotEmpty()) {
            val file = files[0]
            val reader = FileReader()

            reader.onload = { loadEvent ->
                try {
                    val arrayBuffer = reader.result as? org.khronos.webgl.ArrayBuffer
                    if (arrayBuffer != null) {
                        val base64String = arrayBufferToBase64(arrayBuffer)
                        continuation.resume(base64String)
                    } else {
                        continuation.resume(null) // Or handle as error if ArrayBuffer is expected
                    }
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                } finally {
                    document.body?.removeChild(input) // Clean up input element
                }
            }

            reader.onerror = { errorEvent ->
                continuation.resumeWithException(Exception("Error reading file"))
                document.body?.removeChild(input) // Clean up input element
            }

            reader.onabort = {
                continuation.cancel()
                document.body?.removeChild(input) // Clean up input element
            }

            reader.readAsArrayBuffer(file) // Read as ArrayBuffer for binary data
        } else {
            continuation.resume(null) // No file selected
            document.body?.removeChild(input) // Clean up input element
        }
    })

    document.body?.appendChild(input) // Add input to the body
    input.click() // Programmatically trigger file selection dialog

    continuation.invokeOnCancellation {
        document.body?.removeChild(input) // Cleanup on cancellation
    }
}


private fun arrayBufferToBase64(buffer: org.khronos.webgl.ArrayBuffer): String {
    val binary = StringBuilder()
    val bytes = Uint8Array(buffer)
    val len = bytes.length
    for (i in 0 until len) {
        binary.append(Char(bytes[i].toInt()))
    }
    return js("btoa(binary)").toString()
}