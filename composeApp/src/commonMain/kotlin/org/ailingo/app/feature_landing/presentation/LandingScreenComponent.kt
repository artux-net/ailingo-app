package org.ailingo.app.feature_landing.presentation

import com.arkivanov.decompose.ComponentContext

class LandingScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToLoginScreen: () -> Unit
) : ComponentContext by componentContext {

    fun onEvent(event: LandingScreenEvent) {
        when (event) {
            LandingScreenEvent.OnNavigateToLoginScreen -> {
                onNavigateToLoginScreen()
            }
        }
    }
}