package org.ailingo.app.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import org.ailingo.app.LoginPage
import org.ailingo.app.features.login.presentation.LoginScreenEvent
import org.ailingo.app.features.login.presentation.LoginViewModel

@Composable
fun NavigationHandler(navController: NavController, loginViewModel: LoginViewModel) {
    val navigationEvent by NavigationController.navigationEvents.collectAsState(null)

    LaunchedEffect(navigationEvent) {
        if (navigationEvent is NavigationEvent.NavigateToLogin) {
            loginViewModel.onEvent(LoginScreenEvent.OnBackToEmptyState)
            navController.navigate(LoginPage)
        }
    }
}