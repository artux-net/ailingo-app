package org.ailingo.app.feature_dictionary.presentation

import org.ailingo.app.feature_dicitionary_predictor.data.PredictorRequest
import org.ailingo.app.feature_dictionary_history.domain.HistoryDictionary

sealed class DictionaryScreenEvents {
    data class SearchWordDefinition(val word: String) : DictionaryScreenEvents()
    data class PredictNextWords(val request: PredictorRequest) : DictionaryScreenEvents()
    data class SaveSearchedWord(val word: HistoryDictionary) : DictionaryScreenEvents()
    data class DeleteFromHistory(val id: Long) : DictionaryScreenEvents()
}