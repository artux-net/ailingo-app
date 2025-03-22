package org.ailingo.app.features.dictionary.historysearch.domain.repository

import kotlinx.coroutines.flow.Flow
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.dictionary.historysearch.data.model.DictionarySearchHistory

interface DictionarySearchHistoryRepository {
    fun getSearchHistory(): Flow<UiState<List<DictionarySearchHistory>>>
    suspend fun insertWordToSearchHistory(word: DictionarySearchHistory)
    suspend fun deleteWordFromSearchHistory(id: Long)
}
