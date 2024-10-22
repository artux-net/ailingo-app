package org.ailingo.app.feature_dictionary_examples.data.model

import kotlinx.serialization.Serializable

@Serializable
data class License(
    val name: String,
    val url: String
)