package org.ailingo.app

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import app.cash.sqldelight.db.SqlDriver
import coil3.compose.setSingletonImageLoaderFactory
import kotlinx.coroutines.Deferred
import org.ailingo.app.core.helper.voice.VoiceToTextParser
import org.ailingo.app.core.presentation.TopAppBarCenter
import org.ailingo.app.core.presentation.TopAppBarWithProfile
import org.ailingo.app.core.presentation.utils.DrawerItems
import org.ailingo.app.core.utils.getAsyncImageLoader
import org.ailingo.app.features.chat.presentation.ChatScreen
import org.ailingo.app.features.dictionary.history.domain.DictionaryRepository
import org.ailingo.app.features.dictionary.main.presentation.DictionaryScreen
import org.ailingo.app.features.dictionary.main.presentation.DictionaryViewModel
import org.ailingo.app.features.introduction.presentation.GetStartedScreen
import org.ailingo.app.features.login.presentation.LoginScreen
import org.ailingo.app.features.login.presentation.LoginScreenEvent
import org.ailingo.app.features.login.presentation.LoginViewModel
import org.ailingo.app.features.registration.presentation.RegisterScreen
import org.ailingo.app.features.registration.presentation.RegisterViewModel
import org.ailingo.app.features.registration.presentation.UploadAvatarScreen
import org.ailingo.app.features.registration.presentation.UploadAvatarViewModel
import org.ailingo.app.features.resetpass.presentation.ResetPasswordScreen
import org.ailingo.app.features.topics.data.Topic
import org.ailingo.app.features.topics.presentation.TopicsScreen
import org.ailingo.app.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun App(
    voiceToTextParser: VoiceToTextParser,
    dictionaryLocalDataBase: Deferred<DictionaryRepository>
) {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel { LoginViewModel() }
    val registerViewModel: RegisterViewModel = viewModel { RegisterViewModel() }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val routesWithoutTopBar = listOf(
        LandingPage::class,
    )

    val routesWithProfileScreen = listOf(
        ChatPage::class,
        DictionaryPage::class,
        TopicsPage::class,
    )

    val routesWithOutNavigationDrawer = listOf(
        LandingPage::class,
        LoginPage::class,
        RegisterPage::class,
        ResetPasswordPage::class,
        SelectPage::class,
    )

    val showTopAppCenter = currentDestination?.let {
        !routesWithoutTopBar.any { routeClass ->
            it.hasRoute(routeClass)
        }
    }

    val showNavigationDrawer = currentDestination?.let {
        !routesWithOutNavigationDrawer.any { routeClass ->
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

    AppTheme {
        Scaffold(
            topBar = {
                if (showTopAppBarWithProfile == true) {
                    TopAppBarWithProfile(loginViewModel = loginViewModel)
                } else {
                    if (showTopAppCenter == true) {
                        TopAppBarCenter()
                    }
                }
            }
        ) { innerPadding ->
            PermanentNavigationDrawer(
                modifier = Modifier,
                drawerContent = {
                    if (showNavigationDrawer == true) {
                        val items = listOf(
                            DrawerItems.ChatMode,
                            DrawerItems.Topics,
                            DrawerItems.Dictionary,
                            DrawerItems.Exit,
                        )
                        val selectedItem = remember { mutableStateOf(items[0]) }

                        PermanentDrawerSheet(
                            modifier = Modifier.padding(top = 64.dp).width(350.dp).fillMaxHeight(),
                            drawerContainerColor = Color.White
                        ) {
                            items.forEach { item ->
                                if (item == DrawerItems.Exit) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                                NavigationDrawerItem(
                                    icon = { Icon(item.icon, contentDescription = null) },
                                    label = { Text(stringResource(item.title)) },
                                    selected = item == selectedItem.value,
                                    onClick = {
                                        selectedItem.value = item
                                        when (item) {
                                            DrawerItems.ChatMode -> navController.navigate(ChatPage)
                                            DrawerItems.Topics -> navController.navigate(TopicsPage)
                                            DrawerItems.Dictionary -> navController.navigate(
                                                DictionaryPage
                                            )

                                            DrawerItems.Exit -> {
                                                navController.navigate(LoginPage)
                                                loginViewModel.onEvent(LoginScreenEvent.OnBackToEmptyState)
                                            }
                                        }
                                    },
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                )
                            }
                        }
                    }
                }
            ) {
                NavHost(
                    navController,
                    startDestination = LoginPage,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<LoginPage> {
                        LoginScreen(
                            onNavigateToChatScreen = {
                                navController.navigate(ChatPage)
                            },
                            onNavigateToResetPasswordScreen = {
                                navController.navigate(ResetPasswordPage)
                            },
                            onNavigateToRegisterScreen = {
                                navController.navigate(RegisterPage)
                            },
                            loginViewModel = loginViewModel
                        )
                    }
                    composable<ChatPage> {
                        ChatScreen(voiceToTextParser = voiceToTextParser)
                    }
                    composable<RegisterPage> {
                        RegisterScreen(
                            onNavigateToLoginScreen = {
                                navController.navigate(LoginPage)
                            },
                            onNavigateToUploadAvatarScreen = { login, password, email, name ->
                                navController.navigate(
                                    UploadAvatarPage(
                                        login,
                                        password,
                                        email,
                                        name
                                    )
                                )
                            },
                            registerViewModel = registerViewModel
                        )
                    }
                    composable<ResetPasswordPage> {
                        ResetPasswordScreen(
                            onNavigateToSelectScreen = {
                                navController.navigate(SelectPage)
                            }
                        )
                    }
                    composable<SelectPage> {
                        GetStartedScreen(
                            onNavigateToLoginScreen = {
                                navController.navigate(LoginPage)
                            },
                            onNavigateToRegisterScreen = {
                                navController.navigate(RegisterPage)
                            }
                        )
                    }
                    composable<UploadAvatarPage> { backStackEntry ->
                        val args = backStackEntry.toRoute<UploadAvatarPage>()
                        val uploadAvatarViewModel: UploadAvatarViewModel =
                            viewModel { UploadAvatarViewModel() }
                        UploadAvatarScreen(
                            login = args.login,
                            password = args.password,
                            email = args.email,
                            name = args.name,
                            onNavigateToRegisterScreen = {
                                navController.navigate(RegisterPage)
                            },
                            onNavigateToChatScreen = {
                                navController.navigate(ChatPage)
                            },
                            uploadAvatarViewModel = uploadAvatarViewModel
                        )
                    }
                    composable<TopicsPage> {
                        TopicsScreen()
                    }
                    composable<DictionaryPage> {
                        val dictionaryViewModel: DictionaryViewModel = viewModel {
                            DictionaryViewModel(
                                dictionaryLocalDataBase
                            )
                        }
                        DictionaryScreen(dictionaryViewModel)
                    }
                }
            }
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

@Composable
expect fun TopicsForDesktopAndWeb(topics: List<Topic>)
