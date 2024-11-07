package org.ailingo.app.features.registration.presentation

sealed class RegisterUiState {
    data class Success(
        val success: Boolean,
        val code: Int,
        val description: String,
        val failure: Boolean
    ) : RegisterUiState()

    data class Error(val message: String) : RegisterUiState()
    data object Loading : RegisterUiState()
    data object Empty : RegisterUiState()
}
