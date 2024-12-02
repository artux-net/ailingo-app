package org.ailingo.app.features.registration.presentation

data class RegisterUiState(
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val name: String = "",
    val isLoginValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val isNameValid: Boolean = true,
    val isLoading: Boolean = false
)