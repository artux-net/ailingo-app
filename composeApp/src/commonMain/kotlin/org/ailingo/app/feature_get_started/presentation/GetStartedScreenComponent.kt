package org.ailingo.app.feature_get_started.presentation

import com.arkivanov.decompose.ComponentContext

class GetStartedScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToLoginScreen: () -> Unit,
    private val onNavigateToRegisterScreen: () -> Unit
): ComponentContext by componentContext  {
    fun onEvent(event: GetStartedScreenEvent) {
        when (event) {
            GetStartedScreenEvent.OnNavigateToLoginScreen -> onNavigateToLoginScreen()
            GetStartedScreenEvent.OnNavigateToRegisterScreen -> onNavigateToRegisterScreen()
        }
    }
}