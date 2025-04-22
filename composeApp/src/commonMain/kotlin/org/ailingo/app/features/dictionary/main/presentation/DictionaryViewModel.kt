package org.ailingo.app.features.dictionary.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.core.presentation.snackbar.SnackbarAction
import org.ailingo.app.core.presentation.snackbar.SnackbarController
import org.ailingo.app.core.presentation.snackbar.SnackbarEvent
import org.ailingo.app.features.dictionary.historysearch.data.model.DictionarySearchHistory
import org.ailingo.app.features.dictionary.historysearch.domain.repository.DictionarySearchHistoryRepository
import org.ailingo.app.features.dictionary.main.data.model.DictionaryData
import org.ailingo.app.features.dictionary.main.domain.repository.DictionaryRepository
import org.ailingo.app.features.dictionary.predictor.data.model.PredictorRequest
import org.ailingo.app.features.dictionary.predictor.data.model.PredictorResponse
import org.ailingo.app.features.dictionary.predictor.domain.repository.PredictWordsRepository
import org.ailingo.app.features.favouritewords.domain.repository.FavouriteWordsRepository

class DictionaryViewModel(
    private val historyDictionarySearchHistoryRepository: Deferred<DictionarySearchHistoryRepository>,
    private val favouriteWordsRepository: FavouriteWordsRepository,
    private val predictorRepository: PredictWordsRepository,
    private val dictionaryRepository: DictionaryRepository,
    word: String?
) : ViewModel() {

    private val _historyOfDictionaryState = MutableStateFlow<UiState<List<DictionarySearchHistory>>>(UiState.Idle())
    val historyOfDictionaryState = _historyOfDictionaryState.asStateFlow()

    private val _favoriteWordsState = MutableStateFlow<UiState<List<String>>>(UiState.Idle())
    val favouriteWordsState = _favoriteWordsState.asStateFlow()

    private val _dictionaryUiState = MutableStateFlow<UiState<DictionaryData>>(UiState.Idle())
    val dictionaryUiState = _dictionaryUiState.asStateFlow()

    private var _predictorState = MutableStateFlow<UiState<PredictorResponse>>(UiState.Idle())
    val predictorState = _predictorState.asStateFlow()

    init {
        if (word != null) {
            onEvent(DictionaryEvents.GetWordInfo(word))
        }
        onEvent(DictionaryEvents.GetSearchHistory)
        onEvent(DictionaryEvents.GetFavouriteWords)
    }

    fun onEvent(event: DictionaryEvents) {
        when (event) {
            is DictionaryEvents.PredictNextWords -> {
                predictNextWords(event.request)
            }

            is DictionaryEvents.SaveSearchedWord -> {
                saveToSearchHistory(event.word)
            }

            is DictionaryEvents.GetWordInfo -> {
                getWordInfo(event.word)
            }

            is DictionaryEvents.DeleteFromSearchHistory -> {
                deleteFromSearchHistory(event.id)
            }

            is DictionaryEvents.AddToFavorites -> {
                addToFavourite(event.word)
            }

            is DictionaryEvents.RemoveFromFavorites -> {
                removeFromFavourite(event.word)
            }

            DictionaryEvents.GetSearchHistory -> {
                loadSearchHistory()
            }

            DictionaryEvents.GetFavouriteWords -> {
                loadFavoriteWords()
            }
        }
    }

    private fun getWordInfo(word: String?) {
        if (!word.isNullOrBlank()) {
            viewModelScope.launch {
                launch {
                    dictionaryRepository.getWordInfo(word).collect { state ->
                        _dictionaryUiState.update { state }
                    }
                }
            }
        }
    }

    private fun predictNextWords(request: PredictorRequest) {
        viewModelScope.launch {
            predictorRepository.predictNextWords(request).collect { state ->
                _predictorState.update { state }
            }
        }
    }

    private fun loadFavoriteWords() {
        viewModelScope.launch {
            favouriteWordsRepository.getFavouriteWords().collect { state ->
                _favoriteWordsState.update { state }
            }
        }
    }

    private fun addToFavourite(word: String) {
        viewModelScope.launch {
            favouriteWordsRepository.addFavouriteWord(word)
            loadFavoriteWords()
            SnackbarController.sendEvent(
                event = SnackbarEvent(
                    message = "$word added to favorites",
                    action = SnackbarAction(
                        name = "Undo",
                        action = {
                            removeFromFavourite(word)
                        }
                    )
                )
            )
        }
    }

    private fun removeFromFavourite(word: String) {
        viewModelScope.launch {
            favouriteWordsRepository.deleteFavouriteWord(word)
            loadFavoriteWords()
            SnackbarController.sendEvent(
                event = SnackbarEvent(
                    message = "$word removed from favorites",
                    action = SnackbarAction(
                        name = "Undo",
                        action = {
                            addToFavourite(word)
                        }
                    )
                )
            )
        }
    }

    private fun loadSearchHistory() {
        viewModelScope.launch {
            historyDictionarySearchHistoryRepository.await().getSearchHistory().collect { state ->
                _historyOfDictionaryState.update { state }
            }
        }
    }

    private fun saveToSearchHistory(word: DictionarySearchHistory) {
        viewModelScope.launch {
            historyDictionarySearchHistoryRepository.await().insertWordToSearchHistory(word)
        }
    }

    private fun deleteFromSearchHistory(id: Long) {
        viewModelScope.launch {
            historyDictionarySearchHistoryRepository.await().deleteWordFromSearchHistory(id)
        }
    }
}
