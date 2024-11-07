package org.ailingo.app.features.registration.data.image

import kotlinx.serialization.Serializable

@Serializable
data class UploadImageResponse(
    val data: Data,
    val status: Int,
    val success: Boolean
)
