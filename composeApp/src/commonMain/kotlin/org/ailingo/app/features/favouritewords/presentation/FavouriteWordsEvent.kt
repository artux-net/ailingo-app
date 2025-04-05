package org.ailingo.app.features.favouritewords.presentation

sealed class FavouriteWordsEvent {
    data object OnGetFavouriteWords : FavouriteWordsEvent()
    data class OnAddFavouriteWord(val word: String) : FavouriteWordsEvent()
    data class OnDeleteFavouriteWord(val word: String) : FavouriteWordsEvent()
}