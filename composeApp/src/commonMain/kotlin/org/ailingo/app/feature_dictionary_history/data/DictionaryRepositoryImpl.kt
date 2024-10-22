package org.ailingo.app.feature_dictionary_history.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.ailingo.app.feature_dictionary_history.domain.DictionaryRepository
import org.ailingo.app.feature_dictionary_history.domain.HistoryDictionary
import org.ailingo.composeApp.database.HistoryDictionaryDatabase

class DictionaryRepositoryImpl(
    db: HistoryDictionaryDatabase
) : DictionaryRepository {

    private val queries = db.historydictionaryQueries

    override fun getDictionaryHistory(): Flow<List<HistoryDictionary>> {
        val response = queries
            .getDictionaryHistory()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { historyEntities ->
                historyEntities.map { historyEntity ->
                    historyEntity.toHistoryDictionary()
                }
            }
        println(response.toString())
        return response
    }

    override suspend fun insertWordToHistory(word: HistoryDictionary) {
        queries.insertDictionaryHistory(
            id = word.id,
            text = word.text
        )
    }

    override suspend fun deleteWordFromHistory(id: Long) {
        queries.deleteFromDictionaryHistory(id)
    }
}