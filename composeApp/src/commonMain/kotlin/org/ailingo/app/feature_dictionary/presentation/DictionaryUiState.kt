package org.ailingo.app.feature_dictionary.presentation

import org.ailingo.app.feature_dictionary.data.model.DictionaryResponse
import org.ailingo.app.feature_dictionary_examples.data.model.WordInfoItem

sealed class DictionaryUiState {
    data object Loading : DictionaryUiState()
    data object Empty : DictionaryUiState()
    data class Success(val response: DictionaryResponse, val responseExample: List<WordInfoItem>?) : DictionaryUiState()
    data class Error(val message: String) : DictionaryUiState()
}