package org.ailingo.app.feature_reset_password.presentation

import com.arkivanov.decompose.ComponentContext

class ResetPasswordScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateGetStartedScreen: () -> Unit
) : ComponentContext by componentContext {
    fun onEvent(event: ResetPasswordEvent) {
        when (event) {
            ResetPasswordEvent.OnNavigateGetStartedScreen -> onNavigateGetStartedScreen()
        }
    }
}