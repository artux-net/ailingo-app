package org.ailingo.app.features.dictionary.examples.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Meaning(
    val antonyms: List<String>? = null,
    val definitions: List<Definition>,
    val partOfSpeech: String,
    val synonyms: List<String>? = null
)
