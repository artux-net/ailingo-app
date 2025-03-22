package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Syn(
    @SerialName("fr")
    val frequency: Int? = null,
    @SerialName("gen")
    val gender: String? = null,
    @SerialName("pos")
    val partOfSpeech: String? = null,
    @SerialName("text")
    val text: String? = null
)