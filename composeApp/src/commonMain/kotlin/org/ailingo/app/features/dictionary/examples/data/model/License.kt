package org.ailingo.app.features.dictionary.examples.data.model

import kotlinx.serialization.Serializable

@Serializable
data class License(
    val name: String? = null,
    val url: String? = null
)