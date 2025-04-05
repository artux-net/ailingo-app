package org.ailingo.app.features.profileupdate.data.model.imageuploader

import kotlinx.serialization.Serializable

@Serializable
data class ImageUploaderResponse(
    val data: ImageUploaderData,
    val success: Boolean,
    val status: Int
)