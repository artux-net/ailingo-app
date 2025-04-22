package org.ailingo.app.features.dictionary.main.domain.repository

import kotlinx.coroutines.flow.Flow
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.dictionary.main.data.model.DictionaryData

interface DictionaryRepository {
    fun getWordInfo(word: String): Flow<UiState<DictionaryData>>
}