package org.ailingo.app.feature_dictionary.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DictionaryResponse(
    val def: List<Def>? = emptyList(),
    val head: Head? = null
)