package org.ailingo.app.features.dictionary.historysearch.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.ailingo.app.AppDatabase
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.di.ErrorMapper
import org.ailingo.app.features.dictionary.historysearch.data.mapper.toHistoryDictionary
import org.ailingo.app.features.dictionary.historysearch.data.model.DictionarySearchHistory
import org.ailingo.app.features.dictionary.historysearch.domain.repository.DictionarySearchHistoryRepository

class DictionarySearchHistorySearchHistoryRepositoryImpl(
    db: AppDatabase,
    private val errorMapper: ErrorMapper
) : DictionarySearchHistoryRepository {

    private val queries = db.historyDictionaryQueries

    override fun getSearchHistory(): Flow<UiState<List<DictionarySearchHistory>>> = flow {
        emit(UiState.Loading())
        try {
            queries
                .getDictionaryHistory()
                .asFlow()
                .mapToList(Dispatchers.Default)
                .map { historyEntities ->
                    historyEntities.map { historyEntity ->
                        historyEntity.toHistoryDictionary()
                    }
                }.collect {
                    emit(UiState.Success(it))
                }
        } catch (e: Exception) {
            emit(UiState.Error(errorMapper.mapError(e)))
        }
    }

    override suspend fun insertWordToSearchHistory(word: DictionarySearchHistory) {
        queries.insertDictionaryHistory(
            id = word.id,
            text = word.text
        )
    }

    override suspend fun deleteWordFromSearchHistory(id: Long) {
        queries.deleteFromDictionaryHistory(id)
    }
}