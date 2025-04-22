package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DictionaryData(
    val dictionaryApiDevResponses: List<DictionaryApiDevResponse>,
    val yandexDictionaryResponse: YandexDictionaryResponse
)