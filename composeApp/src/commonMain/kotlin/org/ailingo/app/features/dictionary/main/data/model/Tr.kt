package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tr(
    @SerialName("asp")
    val aspect: String? = null,
    @SerialName("fr")
    val frequency: Int? = 0,
    @SerialName("gen")
    val gender: String? = null,
    @SerialName("mean")
    val meanings: List<Mean>? = emptyList(),
    @SerialName("pos")
    val partOfSpeech: String? = null,
    @SerialName("syn")
    val synonyms: List<Syn>? = emptyList(),
    @SerialName("text")
    val text: String? = null
)