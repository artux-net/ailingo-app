package org.ailingo.app.feature_register.data.model_upload_image

import kotlinx.serialization.Serializable

@Serializable
data class Thumb(
    val extension: String,
    val filename: String,
    val mime: String,
    val name: String,
    val url: String
)