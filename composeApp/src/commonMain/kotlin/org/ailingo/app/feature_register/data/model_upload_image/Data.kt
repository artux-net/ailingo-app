package org.ailingo.app.feature_register.data.model_upload_image

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val delete_url: String,
    val display_url: String,
    val expiration: Int,
    val height: Int,
    val id: String,
    val image: Image,
    val size: Int,
    val thumb: Thumb,
    val time: Int,
    val title: String,
    val url: String,
    val url_viewer: String,
    val width: Int
)