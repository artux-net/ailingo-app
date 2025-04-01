package org.ailingo.app.features.profileupdate.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.ailingo.app.di.provideAppContext
import kotlin.coroutines.resume

actual suspend fun selectImage(): String? = withContext(Dispatchers.IO) {
    val context = provideAppContext()
    return@withContext suspendCancellableCoroutine { continuation ->
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val launcher = getImagePickerLauncher(context) { uri ->
            uri?.let {
                val base64String = uriToBase64(context, it)
                continuation.resume(base64String)
            } ?: continuation.resume(null)
        }
        launcher.launch(intent)
        continuation.invokeOnCancellation {
            launcher.unregister()
        }
    }
}

private fun uriToBase64(context: Context, uri: Uri): String? {
    return context.contentResolver.openInputStream(uri)?.use { inputStream ->
        val bytes = inputStream.readBytes()
        Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}

private fun getImagePickerLauncher(
    context: Context,
    onImageSelected: (Uri?) -> Unit
): ActivityResultLauncher<Intent> {
    return (context as? androidx.activity.ComponentActivity)?.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val imageUri: Uri? = result.data?.data
        onImageSelected(imageUri)
    } ?: throw IllegalStateException("Context is not an Activity")
}