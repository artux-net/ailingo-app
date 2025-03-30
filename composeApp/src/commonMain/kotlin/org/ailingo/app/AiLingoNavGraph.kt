package org.ailingo.app

import ChatPage
import DictionaryPage
import FavouriteWordsPage
import GetStartedPage
import LoginPage
import ProfilePage
import ProfileUpdatePage
import RegistrationPage
import TopicsPage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.ailingo.app.core.navigation.presentation.AppNavHost
import org.ailingo.app.core.navigation.presentation.NavigationForDesktop
import org.ailingo.app.core.navigation.presentation.NavigationForMobile
import org.ailingo.app.core.utils.voice.VoiceToTextParser
import org.ailingo.app.core.utils.windowinfo.info.WindowInfo
import org.ailingo.app.core.utils.windowinfo.info.rememberWindowInfo
import org.ailingo.app.features.login.presentation.LoginViewModel
import org.ailingo.app.features.registration.presentation.RegisterUserViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AiLingoNavGraph(
    voiceToTextParser: VoiceToTextParser,
    navController: NavHostController
) {
    val loginViewModel: LoginViewModel = koinViewModel<LoginViewModel>()
    val loginState = loginViewModel.loginState.collectAsStateWithLifecycle().value
    val registerViewModel: RegisterUserViewModel = koinViewModel<RegisterUserViewModel>()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val routesProfileVisible = listOf(
        ChatPage::class,
        DictionaryPage::class,
        TopicsPage::class,
        ProfilePage::class,
        ProfileUpdatePage::class,
        FavouriteWordsPage::class,
    )

    val isTopAppBarWithProfileVisible = currentDestination?.let { dest ->
        routesProfileVisible.any { routeClass ->
            dest.hasRoute(routeClass)
        }
    } ?: false

    val routesWithStandardTopAppBar = listOf(
        LoginPage::class,
        RegistrationPage::class,
        GetStartedPage::class
    )

    val isStandardCenterTopAppBarVisible = currentDestination?.let { dest ->
        routesWithStandardTopAppBar.any { routeClass ->
            dest.hasRoute(routeClass)
        }
    } ?: false

    val windowInfo = rememberWindowInfo()
    if (windowInfo.screenWidthInfo is WindowInfo.WindowType.DesktopWindowInfo) {
        NavigationForDesktop(
            navController = navController,
            isStandardCenterTopAppBarVisible = isStandardCenterTopAppBarVisible,
            isTopAppBarWithProfileVisible = isTopAppBarWithProfileVisible,
            loginViewModel = loginViewModel,
            currentDestination = currentDestination,
            windowInfo = windowInfo,
            loginState = loginState
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                loginViewModel = loginViewModel,
                loginState = loginState,
                voiceToTextParser = voiceToTextParser,
                registrationViewModel = registerViewModel,
                innerPadding = innerPadding,
                windowInfo = windowInfo
            )
        }
    } else {
        NavigationForMobile(
            navController = navController,
            currentDestination = currentDestination,
            isStandardCenterTopAppBarVisible = isStandardCenterTopAppBarVisible,
            isTopAppBarWithProfileVisible = isTopAppBarWithProfileVisible,
            loginState = loginState,
            windowInfo = windowInfo
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                loginViewModel = loginViewModel,
                loginState = loginState,
                voiceToTextParser = voiceToTextParser,
                registrationViewModel = registerViewModel,
                innerPadding = innerPadding,
                windowInfo = windowInfo
            )
        }
    }
}