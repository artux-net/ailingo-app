package org.ailingo.app.features.dictionary.main.presentation

import org.ailingo.app.features.dictionary.examples.data.model.WordInfoItem
import org.ailingo.app.features.dictionary.main.data.model.DictionaryResponse

sealed class DictionaryUiState {
    data object Loading : DictionaryUiState()
    data object Empty : DictionaryUiState()
    data class Success(val response: DictionaryResponse, val responseExample: List<WordInfoItem>?) : DictionaryUiState()
    data class Error(val message: String) : DictionaryUiState()
}
