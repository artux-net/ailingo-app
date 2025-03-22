package org.ailingo.app.features.dictionary.examples.domain.repository

import kotlinx.coroutines.flow.Flow
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.dictionary.examples.data.model.WordInfoItem

interface DictionaryExampleRepository {
    fun getExamples(word: String): Flow<UiState<List<WordInfoItem>>>
}