package org.ailingo.app.core.presentation.navigation

sealed class NavigationEvent {
    object NavigateToLogin : NavigationEvent()
}