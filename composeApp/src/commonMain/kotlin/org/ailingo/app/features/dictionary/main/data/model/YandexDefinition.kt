package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YandexDefinition(
    @SerialName("text")
    val text: String,
    @SerialName("pos")
    val pos: String,
    @SerialName("ts")
    val ts: String,
    @SerialName("tr")
    val tr: List<Translation>
)