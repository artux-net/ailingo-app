package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Syn(
    val fr: Int? = null,
    val gen: String? = null,
    val pos: String? = null,
    val text: String? = null
)
