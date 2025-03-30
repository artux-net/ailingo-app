package org.ailingo.app.features.profileupdate.data.model.imageuploader

import kotlinx.serialization.Serializable

@Serializable
data class ImageUploaderData(
    val id: String,
    val title: String,
    val url_viewer: String,
    val url: String,
    val display_url: String,
    val width: Int,
    val height: Int,
    val size: Int,
    val time: Long,
    val expiration: Long,
    val image: ImgBBImage,
    val thumb: ImgBBImage,
    val medium: ImgBBImage,
    val delete_url: String
)