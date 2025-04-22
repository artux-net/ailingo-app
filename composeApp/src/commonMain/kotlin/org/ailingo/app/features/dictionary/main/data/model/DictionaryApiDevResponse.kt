package org.ailingo.app.features.dictionary.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DictionaryApiDevResponse(
    @SerialName("word")
    val word: String,
    @SerialName("phonetics")
    val phonetics: List<Phonetic>,
    @SerialName("meanings")
    val meanings: List<Meaning>
)