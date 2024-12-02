package org.ailingo.app.features.login.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class LoginUiState {
    @Serializable
    data class Success(
        val id: String,
        val login: String,
        val name: String,
        val email: String,
        val avatar: String,
        val xp: Int,
        val coins: Int,
        val streak: Int,
        val registration: String,
        val lastLoginAt: String
    ) : LoginUiState()

    data class Error(val message: String) : LoginUiState()
    data object Loading : LoginUiState()
    data object Empty : LoginUiState()
}
