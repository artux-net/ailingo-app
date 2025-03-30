package org.ailingo.app.features.favouritewords.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.favouritewords.domain.repository.FavouriteWordsRepository

class FavouriteWordsViewModel(
    private val favouriteWordsRepository: FavouriteWordsRepository
) : ViewModel() {
    private val _favoriteWords = MutableStateFlow<UiState<List<String>>>(UiState.Idle())
    val favoriteWords = _favoriteWords.asStateFlow()

    init {
        loadFavoriteWords()
    }

    fun onEvent(event: FavouriteWordsEvent) {
        when (event) {
            FavouriteWordsEvent.OnGetFavouriteWords -> {
                loadFavoriteWords()
            }

            is FavouriteWordsEvent.OnAddFavouriteWord -> {
                addFavouriteWord(event.word)
            }

            is FavouriteWordsEvent.OnDeleteFavouriteWord -> {
                deleteFavouriteWord(event.word)
            }
        }
    }

    private fun loadFavoriteWords() {
        viewModelScope.launch {
            favouriteWordsRepository.getFavouriteWords().collect { state ->
                _favoriteWords.update { state }
            }
        }
    }

    private fun addFavouriteWord(word: String) {
        viewModelScope.launch {
            favouriteWordsRepository.addFavouriteWord(word)
            loadFavoriteWords()
        }
    }

    private fun deleteFavouriteWord(word: String) {
        viewModelScope.launch {
            favouriteWordsRepository.deleteFavouriteWord(word)
            loadFavoriteWords()
        }
    }
}