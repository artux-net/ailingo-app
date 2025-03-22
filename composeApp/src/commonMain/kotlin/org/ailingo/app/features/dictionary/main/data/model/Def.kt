package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Def(
    @SerialName("pos")
    val partOfSpeech: String,
    @SerialName("text")
    val text: String,
    @SerialName("tr")
    val translations: List<Tr>,
    @SerialName("ts")
    val transcription: String
)