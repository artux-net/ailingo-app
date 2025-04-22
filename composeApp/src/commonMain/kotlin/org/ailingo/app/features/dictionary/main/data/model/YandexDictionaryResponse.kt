package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YandexDictionaryResponse(
    @SerialName("def")
    val def: List<YandexDefinition>,
    @SerialName("nmt_code")
    val nmtCode: Int,
    @SerialName("code")
    val code: Int
)