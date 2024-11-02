package org.ailingo.app.feature_login.presentation

sealed class LoginScreenEvent {
    data class OnLoginUser(val login: String, val password: String): LoginScreenEvent()
    data object OnBackToEmptyState: LoginScreenEvent()
}