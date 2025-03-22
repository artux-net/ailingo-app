package org.ailingo.app.features.favouritewords.domain.repository

import kotlinx.coroutines.flow.Flow
import org.ailingo.app.core.presentation.UiState

interface FavouriteWordsRepository {
    fun getFavouriteWords(): Flow<UiState<List<String>>>
    suspend fun addFavouriteWord(word: String)
    suspend fun deleteFavouriteWord(word: String)
}