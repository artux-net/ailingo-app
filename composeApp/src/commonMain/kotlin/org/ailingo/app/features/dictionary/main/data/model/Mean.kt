package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Mean(
    @SerialName("text")
    val text: String
)