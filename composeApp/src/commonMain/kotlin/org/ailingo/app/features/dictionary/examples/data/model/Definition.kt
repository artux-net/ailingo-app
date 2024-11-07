package org.ailingo.app.features.dictionary.examples.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Definition(
    val antonyms: List<String>,
    val definition: String,
    val example: String? = null,
    val synonyms: List<String>
)
