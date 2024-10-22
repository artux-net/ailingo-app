package org.ailingo.app.feature_get_started.presentation

sealed class GetStartedScreenEvent {
    data object OnNavigateToLoginScreen: GetStartedScreenEvent()
    data object OnNavigateToRegisterScreen: GetStartedScreenEvent()
}