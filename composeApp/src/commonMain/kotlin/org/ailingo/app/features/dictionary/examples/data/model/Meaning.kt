package org.ailingo.app.features.dictionary.examples.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Meaning(
    val antonyms: List<String> = emptyList(),
    val definitions: List<Definition>,
    val partOfSpeech: String? = null,
    val synonyms: List<String>? = emptyList()
)