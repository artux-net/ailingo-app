package org.ailingo.app.feature_landing.presentation

sealed interface LandingScreenEvent {
    data object OnNavigateToLoginScreen : LandingScreenEvent
}