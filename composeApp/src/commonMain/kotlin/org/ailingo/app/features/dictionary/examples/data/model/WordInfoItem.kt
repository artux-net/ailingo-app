package org.ailingo.app.features.dictionary.examples.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WordInfoItem(
    val license: License,
    val meanings: List<Meaning>,
    val phonetics: List<Phonetic>? = emptyList(),
    val sourceUrls: List<String>,
    val word: String
)