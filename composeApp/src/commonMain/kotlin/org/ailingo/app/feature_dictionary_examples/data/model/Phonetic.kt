package org.ailingo.app.feature_dictionary_examples.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Phonetic(
    val audio: String? = null,
    val license: License? = null,
    val sourceUrl: String? = null,
    val text: String
)