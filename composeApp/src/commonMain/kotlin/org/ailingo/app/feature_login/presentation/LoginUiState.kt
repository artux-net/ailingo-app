package org.ailingo.app.feature_login.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class LoginUiState {
    @Serializable
    data class Success(
        val id: String? = "",
        val login: String? = "",
        val avatar: String? = "",
        val xp: Int? = 0,
        val coins: Int? = 0,
        val streak: Int? = 0,
        val registration: String? = "",
        val lastLoginAt: String? = ""
    ) : LoginUiState()

    data class Error(val message: String) : LoginUiState()
    data object Loading : LoginUiState()
    data object Empty : LoginUiState()
}