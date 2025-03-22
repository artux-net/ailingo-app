package org.ailingo.app.core.presentation

sealed class UiState<T> {
    class Success<T>(val data: T) : UiState<T>()
    class Error<T>(val message: String) : UiState<T>()
    class Loading<T> : UiState<T>()
    class Idle<T> : UiState<T>()
}