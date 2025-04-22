package org.ailingo.app

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.exit
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldValue
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import androidx.window.core.layout.WindowWidthSizeClass
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.core.presentation.navigation.NavigationHandler
import org.ailingo.app.core.presentation.snackbar.ObserveAsEvents
import org.ailingo.app.core.presentation.snackbar.SnackbarController
import org.ailingo.app.core.presentation.topappbar.TopAppBarCenter
import org.ailingo.app.core.presentation.topappbar.TopAppBarWithProfile
import org.ailingo.app.features.chat.presentation.ChatScreen
import org.ailingo.app.features.chat.presentation.ChatViewModel
import org.ailingo.app.features.dictionary.main.presentation.DictionaryScreen
import org.ailingo.app.features.dictionary.main.presentation.DictionaryViewModel
import org.ailingo.app.features.favouritewords.presentation.FavouriteScreen
import org.ailingo.app.features.favouritewords.presentation.FavouriteWordsViewModel
import org.ailingo.app.features.login.presentation.LoginScreen
import org.ailingo.app.features.login.presentation.LoginScreenEvent
import org.ailingo.app.features.login.presentation.LoginUiState
import org.ailingo.app.features.login.presentation.LoginViewModel
import org.ailingo.app.features.profile.presentation.ProfileScreen
import org.ailingo.app.features.profileupdate.presentation.ProfileUpdateEvent
import org.ailingo.app.features.profileupdate.presentation.ProfileUpdateScreen
import org.ailingo.app.features.profileupdate.presentation.ProfileUpdateViewModel
import org.ailingo.app.features.registration.presentation.RegisterUserViewModel
import org.ailingo.app.features.registration.presentation.RegistrationEvent
import org.ailingo.app.features.registration.presentation.RegistrationScreen
import org.ailingo.app.features.registration.presentation.email_verification.OtpAction
import org.ailingo.app.features.registration.presentation.email_verification.OtpViewModel
import org.ailingo.app.features.registration.presentation.email_verification.VerifyEmailScreen
import org.ailingo.app.features.topics.presentation.TopicViewModel
import org.ailingo.app.features.topics.presentation.TopicsScreen
import org.ailingo.app.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class, ExperimentalEncodingApi::class)
@Composable
fun AiLingoNavGraph(
    navController: NavHostController
) {
    val loginViewModel: LoginViewModel = koinViewModel<LoginViewModel>()
    val loginState = loginViewModel.loginState.collectAsStateWithLifecycle().value
    val registerViewModel: RegisterUserViewModel = koinViewModel<RegisterUserViewModel>()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val customNavSuiteType = with(adaptiveInfo) {
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
            NavigationSuiteType.NavigationDrawer
        } else {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
        }
    }
    NavigationHandler(navController = navController, loginViewModel = loginViewModel)
    val navigationSuiteState = rememberNavigationSuiteScaffoldState(initialValue = NavigationSuiteScaffoldValue.Hidden)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(
        flow = SnackbarController.events,
        snackbarHostState
    ) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Short
            )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }

    val routesWithNavigationDrawer = listOf(
        ChatPage::class,
        TopicsPage::class,
        DictionaryPage::class,
        ProfilePage::class,
        ProfileUpdatePage::class,
        FavouriteWordsPage::class,
    )

    val isNavigationDrawerVisible = currentDestination?.let { dest ->
        routesWithNavigationDrawer.any { routeClass ->
            dest.hasRoute(routeClass)
        }
    } ?: false

    LaunchedEffect(isNavigationDrawerVisible) {
        scope.launch {
            if (isNavigationDrawerVisible) {
                navigationSuiteState.show()
            } else {
                navigationSuiteState.hide()
            }
        }
    }

    AppTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                if (isNavigationDrawerVisible) {
                    TopAppBarWithProfile(loginState = loginState)
                } else {
                    TopAppBarCenter()
                }
            }
        ) { innerPadding ->
            NavigationSuiteScaffold(
                state = navigationSuiteState,
                navigationSuiteColors = NavigationSuiteDefaults.colors(
                    navigationBarContainerColor = Color.White,
                    navigationDrawerContainerColor = Color.White,
                    navigationRailContainerColor = Color.White
                ),
                modifier = Modifier.padding(innerPadding),
                navigationSuiteItems = {
                    screens.forEach {
                        item(
                            icon = {
                                Icon(
                                    it.icon,
                                    contentDescription = null
                                )
                            },
                            label = { Text(stringResource(it.label), textAlign = TextAlign.Center) },
                            selected = currentDestination?.hasRoute(it.route::class) == true,
                            onClick = {
                                navController.navigate(it.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(LoginPage) {
                                        saveState = true
                                    }
                                }
                            }
                        )
                    }
                    if (adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED || adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM) {
                        item(
                            icon = {
                                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                            },
                            label = {
                                Text(stringResource(Res.string.exit))
                            },
                            selected = false,
                            onClick = {
                                loginViewModel.onEvent(LoginScreenEvent.OnBackToEmptyState)
                                navController.navigate(LoginPage) {
                                    popUpTo(0)
                                }
                            }
                        )
                    }
                },
                layoutType = customNavSuiteType
            ) {
                NavHost(navController, startDestination = LoginPage) {
                    composable<LoginPage> {
                        LoginScreen(
                            loginState = loginState, onNavigateToHomeScreen = {
                                navController.navigate(TopicsPage)
                            }, onNavigateToRegisterScreen = {
                                navController.navigate(RegistrationPage)
                            }, onEvent = { event ->
                                loginViewModel.onEvent(event)
                            }
                        )
                    }
                    composable<TopicsPage> {
                        val topicsViewModel = koinViewModel<TopicViewModel>()
                        val topicsUiState = topicsViewModel.topicState.collectAsStateWithLifecycle().value
                        TopicsScreen(
                            topicsUiState = topicsUiState,
                            onTopicClick = { topicName, topicImage ->
                                navController.navigate(
                                    ChatPage(
                                        topicName = topicName,
                                        topicImage = topicImage
                                    )
                                )
                            }
                        )
                    }
                    composable<ChatPage> { backStackEntry ->
                        val args = backStackEntry.toRoute<ChatPage>()
                        val chatViewModel: ChatViewModel = koinViewModel { parametersOf(args.topicName) }
                        val chatUiState = chatViewModel.chatState.collectAsStateWithLifecycle().value
                        val messagesState = chatViewModel.messages.collectAsStateWithLifecycle().value

                        LaunchedEffect(chatUiState) {
                            if (chatUiState is UiState.Idle) {
                                loginViewModel.onEvent(LoginScreenEvent.RefreshUserInfo)
                            }
                        }

                        ChatScreen(
                            topicName = args.topicName,
                            topicImage = args.topicImage,
                            chatUiState = chatUiState,
                            messagesState = messagesState,
                            onEvent = { event ->
                                chatViewModel.onEvent(event)
                            }
                        )
                    }
                    composable<RegistrationPage> {
                        val pendingRegistrationState = registerViewModel.pendingRegistrationUiState.collectAsStateWithLifecycle().value
                        RegistrationScreen(
                            onNavigateToLoginPage = {
                                navController.navigate(LoginPage)
                            },
                            onNavigateToVerifyEmail = { email, password ->
                                navController.navigate(VerifyEmailPage(email = email, password = password))
                            },
                            pendingRegistrationState = pendingRegistrationState,
                            onEvent = { event ->
                                registerViewModel.onEvent(event)
                            }
                        )
                    }
                    composable<ProfilePage> {
                        ProfileScreen(
                            loginState = loginState,
                            onExit = {
                                loginViewModel.onEvent(LoginScreenEvent.OnBackToEmptyState)
                                navController.navigate(LoginPage) {
                                    popUpTo(0)
                                }
                            }, onNavigateProfileChange = { name, email, avatar ->
                                navController.navigate(
                                    ProfileUpdatePage(
                                        name, email, avatar
                                    )
                                )
                            }
                        )
                    }
                    composable<ProfileUpdatePage> {
                        if (loginState is LoginUiState.Success) {
                            val profileUpdateViewModel = koinViewModel<ProfileUpdateViewModel>()
                            val profileUpdateUiState = profileUpdateViewModel.profileUpdateUiState.collectAsStateWithLifecycle().value
                            val uploadAvatarState = profileUpdateViewModel.uploadAvatarState.collectAsStateWithLifecycle().value
                            ProfileUpdateScreen(
                                profileUpdateUiState = profileUpdateUiState,
                                uploadAvatarState = uploadAvatarState,
                                onProfileUpdate = {
                                    profileUpdateViewModel.onEvent(ProfileUpdateEvent.OnUpdateProfile(it))
                                },
                                onReLoginUser = { newLogin, currentPassword, newPassword, passwordChanged ->
                                    val passwordToUse = if (passwordChanged) {
                                        newPassword
                                    } else currentPassword
                                    loginViewModel.onEvent(
                                        LoginScreenEvent.OnLoginUser(
                                            newLogin,
                                            passwordToUse
                                        )
                                    )
                                },
                                onNavigateProfileScreen = {
                                    navController.navigate(ProfilePage)
                                },
                                onBackToEmptyState = {
                                    profileUpdateViewModel.onEvent(ProfileUpdateEvent.OnBackToEmptyState)
                                },
                                name = loginState.user.name,
                                email = loginState.user.email,
                                avatar = loginState.user.avatar,
                                onUploadNewAvatar = {
                                    profileUpdateViewModel.onEvent(ProfileUpdateEvent.OnUploadAvatar(it))
                                }
                            )
                        }
                    }
                    composable<FavouriteWordsPage> {
                        val favouriteWordsViewModel = koinViewModel<FavouriteWordsViewModel>()
                        val favouriteWordsState = favouriteWordsViewModel.favoriteWords.collectAsStateWithLifecycle().value
                        FavouriteScreen(
                            favouriteWordsState = favouriteWordsState,
                            onNavigateToDictionaryScreen = { word ->
                                navController.navigate(DictionaryPage(word))
                            },
                            onEvent = { event ->
                                favouriteWordsViewModel.onEvent(event)
                            }
                        )
                    }
                    composable<DictionaryPage> { backStackEntry ->
                        val args = backStackEntry.toRoute<DictionaryPage>()
                        val dictionaryViewModel: DictionaryViewModel = koinViewModel { parametersOf(args.word) }
                        val dictionaryState = dictionaryViewModel.dictionaryUiState.collectAsStateWithLifecycle().value
                        val searchHistoryState = dictionaryViewModel.historyOfDictionaryState.collectAsStateWithLifecycle().value
                        val favoriteDictionaryState = dictionaryViewModel.favouriteWordsState.collectAsStateWithLifecycle().value
                        val predictorState = dictionaryViewModel.predictorState.collectAsStateWithLifecycle().value
                        DictionaryScreen(
                            dictionaryState,
                            searchHistoryState,
                            favoriteDictionaryState,
                            predictorState,
                            onEvent = { event ->
                                dictionaryViewModel.onEvent(event)
                            }
                        )
                    }
                    composable<VerifyEmailPage> { backStack ->
                        val args = backStack.toRoute<VerifyEmailPage>()
                        val registrationState = registerViewModel.registrationUiState.collectAsStateWithLifecycle().value
                        val otpViewModel: OtpViewModel = koinViewModel<OtpViewModel>()
                        val otpState = otpViewModel.state.collectAsStateWithLifecycle().value
                        val focusRequesters = remember {
                            List(6) { FocusRequester() }
                        }
                        val focusManager = LocalFocusManager.current
                        val keyboardManager = LocalSoftwareKeyboardController.current

                        LaunchedEffect(otpState.focusedIndex) {
                            otpState.focusedIndex?.let { index ->
                                focusRequesters.getOrNull(index)?.requestFocus()
                            }
                        }

                        LaunchedEffect(otpState.code, keyboardManager) {
                            val allNumbersEntered = otpState.code.none { it == null }
                            if (allNumbersEntered) {
                                focusRequesters.forEach {
                                    it.freeFocus()
                                }
                                focusManager.clearFocus()
                                keyboardManager?.hide()
                                val verificationCode = otpState.code.joinToString("")
                                registerViewModel.onEvent(
                                    RegistrationEvent.OnVerifyEmail(
                                        args.email,
                                        verificationCode
                                    )
                                )
                            }
                        }

                        VerifyEmailScreen(
                            email = args.email,
                            otpState = otpState,
                            focusRequesters = focusRequesters,
                            onAction = { action ->
                                when (action) {
                                    is OtpAction.OnEnterNumber -> {
                                        if (action.number != null) {
                                            focusRequesters[action.index].freeFocus()
                                        }
                                    }

                                    else -> Unit
                                }
                                otpViewModel.onAction(action)
                            },
                            registrationState = registrationState,
                            onNavigateChatScreen = {
                                loginViewModel.onEvent(LoginScreenEvent.OnLoginUser(args.email, args.password))
                                navController.navigate(ChatPage)
                            },
                            modifier = Modifier,
                            onNavigateBack = {
                                navController.navigateUp()
                            }
                        )
                    }
                }
            }
        }
    }
}
