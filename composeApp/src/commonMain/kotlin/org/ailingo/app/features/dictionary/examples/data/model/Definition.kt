package org.ailingo.app.features.dictionary.examples.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Definition(
    @SerialName("antonyms")
    val antonyms: List<String> = emptyList(),
    @SerialName("definition")
    val definition: String? = null,
    @SerialName("example")
    val example: String? = null,
    @SerialName("synonyms")
    val synonyms: List<String> = emptyList()
)