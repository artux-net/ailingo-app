package org.ailingo.app.features.dictionary.main.presentation

import org.ailingo.app.features.dictionary.historysearch.data.model.DictionarySearchHistory
import org.ailingo.app.features.dictionary.predictor.data.model.PredictorRequest

sealed class DictionaryEvents {
    data class GetWordInfo(val word: String?) : DictionaryEvents()
    data class PredictNextWords(val request: PredictorRequest) : DictionaryEvents()
    data class SaveSearchedWord(val word: DictionarySearchHistory) : DictionaryEvents()
    data class DeleteFromSearchHistory(val id: Long) : DictionaryEvents()
    data class AddToFavorites(val word: String) : DictionaryEvents()
    data class RemoveFromFavorites(val word: String) : DictionaryEvents()
    data object GetSearchHistory: DictionaryEvents()
    data object GetFavouriteWords: DictionaryEvents()
}