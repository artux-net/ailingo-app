package org.ailingo.app.features.profileupdate.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.util.Base64
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual suspend fun selectImage(): String? = withContext(Dispatchers.IO) {
    val selectedFile = chooseFileWithDialog()
    return@withContext selectedFile?.let { encodeFileToBase64(it) }
}

private suspend fun chooseFileWithDialog(): File? = suspendCancellableCoroutine { continuation ->
    SwingUtilities.invokeLater {
        try {
            val fileChooser = JFileChooser()
            val filter = FileNameExtensionFilter("Image files", "png", "jpg")
            fileChooser.fileFilter = filter
            val result = fileChooser.showOpenDialog(null)

            if (result == JFileChooser.APPROVE_OPTION) {
                continuation.resume(fileChooser.selectedFile)
            } else {
                continuation.resume(null)
            }
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }
    continuation.invokeOnCancellation {}
}


private fun encodeFileToBase64(file: File): String {
    val fileContent = Files.readAllBytes(file.toPath())
    return Base64.getEncoder().encodeToString(fileContent)
}


