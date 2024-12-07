package org.ailingo.app.features.dictionary.history.domain

sealed class HistoryDictionaryUiState {
    data object Loading : HistoryDictionaryUiState()
    data class Success(val history: List<HistoryDictionary>) : HistoryDictionaryUiState()
    data class Error(val message: String) : HistoryDictionaryUiState()
}