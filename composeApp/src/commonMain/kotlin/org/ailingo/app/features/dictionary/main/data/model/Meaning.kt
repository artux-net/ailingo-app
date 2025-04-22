package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meaning(
    @SerialName("partOfSpeech")
    val partOfSpeech: String,
    @SerialName("definitions")
    val definitions: List<Definition>
)