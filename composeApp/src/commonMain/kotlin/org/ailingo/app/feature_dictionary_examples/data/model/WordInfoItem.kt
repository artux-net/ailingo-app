package org.ailingo.app.feature_dictionary_examples.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WordInfoItem(
    val license: License,
    val meanings: List<Meaning>,
    val phonetic: String,
    val phonetics: List<Phonetic>,
    val sourceUrls: List<String>,
    val word: String
)