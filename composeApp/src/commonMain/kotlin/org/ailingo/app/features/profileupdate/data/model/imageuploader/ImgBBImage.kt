package org.ailingo.app.features.profileupdate.data.model.imageuploader

import kotlinx.serialization.Serializable

@Serializable
data class ImgBBImage(
    val filename: String,
    val name: String,
    val mime: String,
    val extension: String,
    val url: String
)