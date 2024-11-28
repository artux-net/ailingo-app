package org.ailingo.app.features.registration.presentation.uploadavatar

sealed class RegisterApiState {
    data class Success(
        val success: Boolean,
        val code: Int,
        val description: String,
        val failure: Boolean
    ) : RegisterApiState()

    data class Error(val message: String) : RegisterApiState()
    data object Loading : RegisterApiState()
    data object Empty : RegisterApiState()
}
