package org.ailingo.app.features.dictionary.history.domain

import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun getDictionaryHistory(): Flow<List<HistoryDictionary>>
    suspend fun insertWordToHistory(word: HistoryDictionary)
    suspend fun deleteWordFromHistory(id: Long)
}
