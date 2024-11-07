package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Def(
    val pos: String,
    val text: String,
    val tr: List<Tr>,
    val ts: String
)
