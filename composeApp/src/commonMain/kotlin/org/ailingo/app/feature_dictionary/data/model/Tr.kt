package org.ailingo.app.feature_dictionary.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Tr(
    val asp: String? = null,
    val fr: Int? = 0,
    val gen: String? = null,
    val mean: List<Mean>? = emptyList(),
    val pos: String? = null,
    val syn: List<Syn>? = emptyList(),
    val text: String? = null
)