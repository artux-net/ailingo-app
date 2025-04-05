package org.ailingo.app.features.profileupdate.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual suspend fun selectImage(): String? = withContext(Dispatchers.Default) {
    return@withContext null
}