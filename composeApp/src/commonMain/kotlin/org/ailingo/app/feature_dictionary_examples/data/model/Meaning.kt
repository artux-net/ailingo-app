package org.ailingo.app.feature_dictionary_examples.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Meaning(
    val antonyms: List<String>? = null,
    val definitions: List<Definition>,
    val partOfSpeech: String,
    val synonyms: List<String>? = null
)