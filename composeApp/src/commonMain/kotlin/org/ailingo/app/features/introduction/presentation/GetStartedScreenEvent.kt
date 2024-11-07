package org.ailingo.app.features.introduction.presentation

sealed class GetStartedScreenEvent {
    data object OnNavigateToLoginScreen : GetStartedScreenEvent()
    data object OnNavigateToRegisterScreen : GetStartedScreenEvent()
}
