package org.ailingo.app.feature_register.data.model_upload_image

import kotlinx.serialization.Serializable

@Serializable
data class UploadImageResponse(
    val data: Data,
    val status: Int,
    val success: Boolean
)