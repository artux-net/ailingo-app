package org.ailingo.app.feature_dictionary.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Def(
    val pos: String,
    val text: String,
    val tr: List<Tr>,
    val ts: String
)