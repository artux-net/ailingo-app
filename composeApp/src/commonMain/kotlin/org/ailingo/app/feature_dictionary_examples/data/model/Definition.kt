package org.ailingo.app.feature_dictionary_examples.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Definition(
    val antonyms: List<String>,
    val definition: String,
    val example: String? = null,
    val synonyms: List<String>
)