package org.ailingo.app.features.login.presentation

sealed class LoginScreenEvent {
    data class OnLoginUser(val login: String, val password: String) : LoginScreenEvent()
    object RefreshUserInfo : LoginScreenEvent()
    data object OnBackToEmptyState : LoginScreenEvent()
}