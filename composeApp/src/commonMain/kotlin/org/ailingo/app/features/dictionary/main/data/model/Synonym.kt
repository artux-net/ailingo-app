package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Synonym(
    @SerialName("text")
    val text: String,
    @SerialName("pos")
    val pos: String,
    @SerialName("gen")
    val gen: String?,
    @SerialName("fr")
    val fr: Int
)