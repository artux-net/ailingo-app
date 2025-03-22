package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DictionaryResponse(
    @SerialName("def")
    val definitions: List<Def> = emptyList(),
    @SerialName("head")
    val head: Head? = null
)