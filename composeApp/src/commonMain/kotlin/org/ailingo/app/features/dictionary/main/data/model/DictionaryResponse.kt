package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DictionaryResponse(
    val def: List<Def>? = emptyList(),
    val head: Head? = null
)
