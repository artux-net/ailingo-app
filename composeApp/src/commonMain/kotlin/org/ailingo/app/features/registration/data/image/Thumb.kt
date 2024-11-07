package org.ailingo.app.features.registration.data.image

import kotlinx.serialization.Serializable

@Serializable
data class Thumb(
    val extension: String,
    val filename: String,
    val mime: String,
    val name: String,
    val url: String
)
