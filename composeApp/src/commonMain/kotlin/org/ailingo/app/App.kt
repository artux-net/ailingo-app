package org.ailingo.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.cash.sqldelight.db.SqlDriver
import coil3.compose.setSingletonImageLoaderFactory
import kotlinx.coroutines.Deferred
import org.ailingo.app.core.navigation.presentation.AppNavHost
import org.ailingo.app.core.navigation.presentation.NavigationForDesktop
import org.ailingo.app.core.navigation.presentation.NavigationForMobile
import org.ailingo.app.core.utils.coil.getAsyncImageLoader
import org.ailingo.app.core.utils.voice.VoiceToTextParser
import org.ailingo.app.core.utils.windowinfo.info.WindowInfo
import org.ailingo.app.core.utils.windowinfo.info.rememberWindowInfo
import org.ailingo.app.features.dictionary.history.domain.DictionaryRepository
import org.ailingo.app.features.login.presentation.LoginViewModel
import org.ailingo.app.features.registration.presentation.RegisterViewModel
import org.ailingo.app.features.registration.presentation.UploadAvatarViewModel

@Composable
internal fun App(
    voiceToTextParser: VoiceToTextParser,
    dictionaryLocalDataBase: Deferred<DictionaryRepository>
) {
    val windowInfo = rememberWindowInfo()

    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel { LoginViewModel() }
    val registerViewModel: RegisterViewModel = viewModel { RegisterViewModel() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val routesWithoutTopBar = listOf(
        LandingPage::class,
    )

    val routesWithProfileScreen = listOf(
        ChatPage::class,
        DictionaryPage::class,
        TopicsPage::class,
    )

    val showTopAppCenter = currentDestination?.let {
        !routesWithoutTopBar.any { routeClass ->
            it.hasRoute(routeClass)
        }
    }


    val showTopAppBarWithProfile = currentDestination?.let {
        routesWithProfileScreen.any { routeClass ->
            it.hasRoute(routeClass)
        }
    }

    // Coil
    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }
    if (windowInfo.screenWidthInfo is WindowInfo.WindowType.DesktopWindowInfo) {
        NavigationForDesktop(
            navController,
            showTopAppCenter,
            showTopAppBarWithProfile,
            loginViewModel,
            currentDestination,
        ) { innerPadding ->
            AppNavHost(
                navController,
                loginViewModel,
                voiceToTextParser,
                registerViewModel,
                dictionaryLocalDataBase,
                innerPadding,
            )
        }
    } else {
        NavigationForMobile(
            navController,
            currentDestination,
            showTopAppCenter,
            showTopAppBarWithProfile,
            loginViewModel,
        ) { innerPadding ->
            AppNavHost(
                navController,
                loginViewModel,
                voiceToTextParser,
                registerViewModel,
                dictionaryLocalDataBase,
                innerPadding,
            )
        }
    }
}

internal expect fun openUrl(url: String?)
internal expect fun getPlatformName(): String
internal expect fun playSound(sound: String)

@Composable
internal expect fun getConfiguration(): Pair<Int, Int>

expect class DriverFactory {
    suspend fun createDriver(): SqlDriver
}

expect suspend fun selectImageWebAndDesktop(): String?

@Composable
expect fun UploadAvatarForPhone(
    uploadAvatarViewModel: UploadAvatarViewModel,
    login: String,
    password: String,
    email: String,
    name: String,
    onNavigateToRegisterScreen: () -> Unit
)

