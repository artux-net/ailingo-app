package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Definition(
    @SerialName("definition")
    val definition: String,
    @SerialName("synonyms")
    val synonyms: List<String>,
    @SerialName("antonyms")
    val antonyms: List<String>,
    @SerialName("example")
    val example: String?
)
