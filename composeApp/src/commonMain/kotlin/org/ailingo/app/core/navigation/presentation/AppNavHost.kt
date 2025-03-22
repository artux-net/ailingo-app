package org.ailingo.app.core.navigation.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.ailingo.app.ChatPage
import org.ailingo.app.DictionaryPage
import org.ailingo.app.FavouriteWordsPage
import org.ailingo.app.GetStartedPage
import org.ailingo.app.LoginPage
import org.ailingo.app.ProfilePage
import org.ailingo.app.ProfileUpdatePage
import org.ailingo.app.RegistrationPage
import org.ailingo.app.TopicsPage
import org.ailingo.app.VerifyEmailPage
import org.ailingo.app.core.utils.voice.VoiceToTextParser
import org.ailingo.app.core.utils.windowinfo.info.WindowInfo
import org.ailingo.app.features.chat.presentation.ChatScreen
import org.ailingo.app.features.dictionary.main.presentation.DictionaryEvents
import org.ailingo.app.features.dictionary.main.presentation.DictionaryScreen
import org.ailingo.app.features.dictionary.main.presentation.DictionaryViewModel
import org.ailingo.app.features.favouritewords.FavouriteScreen
import org.ailingo.app.features.favouritewords.FavouriteWordsEvent
import org.ailingo.app.features.favouritewords.FavouriteWordsViewModel
import org.ailingo.app.features.introduction.presentation.GetStartedScreen
import org.ailingo.app.features.login.presentation.LoginScreen
import org.ailingo.app.features.login.presentation.LoginScreenEvent
import org.ailingo.app.features.login.presentation.LoginUiState
import org.ailingo.app.features.login.presentation.LoginViewModel
import org.ailingo.app.features.profile.presentation.ProfileScreen
import org.ailingo.app.features.profileupdate.presentation.ProfileUpdateEvent
import org.ailingo.app.features.profileupdate.presentation.ProfileUpdateScreen
import org.ailingo.app.features.profileupdate.presentation.ProfileUpdateViewModel
import org.ailingo.app.features.registration.data.model.RegistrationRequest
import org.ailingo.app.features.registration.presentation.RegisterUserViewModel
import org.ailingo.app.features.registration.presentation.RegistrationEvent
import org.ailingo.app.features.registration.presentation.RegistrationScreen
import org.ailingo.app.features.registration.presentation.email_verification.OtpAction
import org.ailingo.app.features.registration.presentation.email_verification.OtpViewModel
import org.ailingo.app.features.registration.presentation.email_verification.VerifyEmailScreen
import org.ailingo.app.features.topics.presentation.TopicViewModel
import org.ailingo.app.features.topics.presentation.TopicsScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

data class ComposableAnimationSpecs(
    val enter: EnterTransition,
    val exit: ExitTransition,
    val popEnter: EnterTransition,
    val popExit: ExitTransition
)

fun AnimatedContentTransitionScope<*>.defaultComposableAnimation(): ComposableAnimationSpecs {
    val enterTransition = fadeIn(
        animationSpec = tween(600)
    )
    val exitTransition = fadeOut(
        animationSpec = tween(600)
    )
    val popEnterTransition = fadeIn(
        animationSpec = tween(600),
    )
    val popExitTransition = fadeOut(
        animationSpec = tween(600),
    )

    return ComposableAnimationSpecs(
        enter = enterTransition,
        exit = exitTransition,
        popEnter = popEnterTransition,
        popExit = popExitTransition
    )
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    loginState: LoginUiState,
    voiceToTextParser: VoiceToTextParser,
    registrationViewModel: RegisterUserViewModel,
    innerPadding: PaddingValues,
    windowInfo: WindowInfo,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = LoginPage,
        modifier = modifier.padding(innerPadding)
    ) {
        composable<LoginPage>(
            enterTransition = { this.defaultComposableAnimation().enter },
            exitTransition = { this.defaultComposableAnimation().exit },
            popEnterTransition = { this.defaultComposableAnimation().popEnter },
            popExitTransition = { this.defaultComposableAnimation().popExit }
        ) {
            LoginScreen(
                loginState = loginState,
                onBackToEmptyState = {
                    loginViewModel.onEvent(LoginScreenEvent.OnBackToEmptyState)
                },
                onNavigateToHomeScreen = {
                    navController.navigate(ChatPage) {
                        popUpTo(0)
                    }
                },
                onLoginUser = { email, password ->
                    loginViewModel.onEvent(LoginScreenEvent.OnLoginUser(email, password))
                },
                onNavigateToRegisterScreen = {
                    navController.navigate(RegistrationPage)
                }
            )
        }
        composable<ChatPage>(
            enterTransition = { this.defaultComposableAnimation().enter },
            exitTransition = { this.defaultComposableAnimation().exit },
            popEnterTransition = { this.defaultComposableAnimation().popEnter },
            popExitTransition = { this.defaultComposableAnimation().popExit }
        ) {
            ChatScreen(
                voiceToTextParser = voiceToTextParser,
                windowInfo = windowInfo,
                login = loginViewModel.login,
                password = loginViewModel.password
            )
        }
        composable<RegistrationPage>(
            enterTransition = { this.defaultComposableAnimation().enter },
            exitTransition = { this.defaultComposableAnimation().exit },
            popEnterTransition = { this.defaultComposableAnimation().popEnter },
            popExitTransition = { this.defaultComposableAnimation().popExit }
        ) {
            val pendingRegistrationState =
                registrationViewModel.pendingRegistrationUiState.collectAsState()
            RegistrationScreen(
                onNavigateToLoginPage = {
                    navController.navigate(LoginPage)
                },
                onNavigateToVerifyEmail = { email, password ->
                    navController.navigate(VerifyEmailPage(email, password))
                },
                onRegisterUser = { login, password, email, name ->
                    registrationViewModel.onEvent(
                        RegistrationEvent.OnRegisterUser(
                            RegistrationRequest(
                                login = login,
                                password = password,
                                email = email,
                                name = name
                            )
                        )
                    )
                },
                pendingRegistrationState = pendingRegistrationState.value,
                onBackRegistrationState = {
                    registrationViewModel.onEvent(RegistrationEvent.OnBackToEmptyState)
                }
            )
        }
        composable<GetStartedPage>(
            enterTransition = { this.defaultComposableAnimation().enter },
            exitTransition = { this.defaultComposableAnimation().exit },
            popEnterTransition = { this.defaultComposableAnimation().popEnter },
            popExitTransition = { this.defaultComposableAnimation().popExit }
        ) {
            GetStartedScreen(
                onNavigateToLoginScreen = {
                    navController.navigate(LoginPage)
                },
                onNavigateToRegisterScreen = {
                    navController.navigate(RegistrationPage)
                }
            )
        }
        composable<TopicsPage>(
            enterTransition = { this.defaultComposableAnimation().enter },
            exitTransition = { this.defaultComposableAnimation().exit },
            popEnterTransition = { this.defaultComposableAnimation().popEnter },
            popExitTransition = { this.defaultComposableAnimation().popExit }
        ) {
            val topicsViewModel: TopicViewModel = koinViewModel<TopicViewModel>()
            val topicUiState = topicsViewModel.topicState.collectAsStateWithLifecycle().value
            TopicsScreen(windowInfo = windowInfo, topicUiState)
        }
        composable<DictionaryPage>(
            enterTransition = { this.defaultComposableAnimation().enter },
            exitTransition = { this.defaultComposableAnimation().exit },
            popEnterTransition = { this.defaultComposableAnimation().popEnter },
            popExitTransition = { this.defaultComposableAnimation().popExit }
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<DictionaryPage>()
            val dictionaryViewModel: DictionaryViewModel = koinViewModel<DictionaryViewModel>()
            if (args.word != "" && args.word != null) {
                dictionaryViewModel.onEvent(DictionaryEvents.GetWordInfo(args.word))
            }
            val dictionaryState = dictionaryViewModel.dictionaryUiState.collectAsStateWithLifecycle().value
            val examplesState = dictionaryViewModel.examplesUiState.collectAsStateWithLifecycle().value
            val searchHistoryState = dictionaryViewModel.historyOfDictionaryState.collectAsStateWithLifecycle().value
            val favoriteDictionaryState = dictionaryViewModel.favouriteWordsState.collectAsStateWithLifecycle().value
            val predictorState = dictionaryViewModel.predictorState.collectAsStateWithLifecycle().value
            DictionaryScreen(
                dictionaryState,
                examplesState,
                searchHistoryState,
                favoriteDictionaryState,
                predictorState,
                onGetWordInfo = {
                    dictionaryViewModel.onEvent(DictionaryEvents.GetWordInfo(it))
                },
                onPredictNextWords = {
                    dictionaryViewModel.onEvent(DictionaryEvents.PredictNextWords(it))
                },
                onSaveSearchedWord = {
                    dictionaryViewModel.onEvent(DictionaryEvents.SaveSearchedWord(it))
                },
                onAddToFavourite = {
                    dictionaryViewModel.onEvent(DictionaryEvents.AddToFavorites(it))
                },
                onRemoveFromFavourites = {
                    dictionaryViewModel.onEvent(DictionaryEvents.RemoveFromFavorites(it))
                }
            )
        }
        composable<ProfilePage>(
            enterTransition = { this.defaultComposableAnimation().enter },
            exitTransition = { this.defaultComposableAnimation().exit },
            popEnterTransition = { this.defaultComposableAnimation().popEnter },
            popExitTransition = { this.defaultComposableAnimation().popExit }
        ) {
            ProfileScreen(
                loginUiState = loginState,
                windowInfo = windowInfo,
                onExit = {
                    loginViewModel.onEvent(LoginScreenEvent.OnBackToEmptyState)
                    navController.navigate(LoginPage) {
                        popUpTo(0)
                    }
                },
                onNavigateProfileChange = { name, email, avatar ->
                    navController.navigate(
                        ProfileUpdatePage(
                            name, email, avatar
                        )
                    )
                }
            )
        }
        composable<ProfileUpdatePage>(
            enterTransition = { this.defaultComposableAnimation().enter },
            exitTransition = { this.defaultComposableAnimation().exit },
            popEnterTransition = { this.defaultComposableAnimation().popEnter },
            popExitTransition = { this.defaultComposableAnimation().popExit }
        ) {
            if (loginState is LoginUiState.Success) {
                val profileUpdateViewModel = koinViewModel<ProfileUpdateViewModel>()
                val profileUpdateUiState = profileUpdateViewModel.profileUpdateUiState.collectAsStateWithLifecycle().value
                ProfileUpdateScreen(
                    profileUpdateUiState = profileUpdateUiState,
                    onProfileUpdate = {
                        profileUpdateViewModel.onEvent(ProfileUpdateEvent.OnUpdateProfile(it))
                    },
                    onReLoginUser = { newLogin, currentPassword, newPassword, passwordChanged ->
                        val passwordToUse = if (passwordChanged) {
                            newPassword
                        } else currentPassword
                        loginViewModel.onEvent(LoginScreenEvent.OnLoginUser(newLogin, passwordToUse))
                    },
                    onNavigateProfileScreen = {
                        navController.navigate(ProfilePage)
                    },
                    onBackToEmptyState = {
                        profileUpdateViewModel.onEvent(ProfileUpdateEvent.OnBackToEmptyState)
                    },
                    name = loginState.user.name,
                    email = loginState.user.email,
                    avatar = loginState.user.avatar
                )
            }
        }
        composable<FavouriteWordsPage>(
            enterTransition = { this.defaultComposableAnimation().enter },
            exitTransition = { this.defaultComposableAnimation().exit },
            popEnterTransition = { this.defaultComposableAnimation().popEnter },
            popExitTransition = { this.defaultComposableAnimation().popExit }
        ) {
            val favouriteWordsViewModel = koinViewModel<FavouriteWordsViewModel>()
            val favouriteWordsState = favouriteWordsViewModel.favoriteWords.collectAsStateWithLifecycle().value
            FavouriteScreen(
                favouriteWordsState = favouriteWordsState,
                onNavigateToDictionaryScreen = { word ->
                    navController.navigate(DictionaryPage(word))
                },
                onDeleteFavouriteWord = { word ->
                    favouriteWordsViewModel.onEvent(FavouriteWordsEvent.OnDeleteFavouriteWord(word))
                }
            )
        }
        composable<VerifyEmailPage>(
            enterTransition = { this.defaultComposableAnimation().enter },
            exitTransition = { this.defaultComposableAnimation().exit },
            popEnterTransition = { this.defaultComposableAnimation().popEnter },
            popExitTransition = { this.defaultComposableAnimation().popExit }
        ) { backStack ->
            val args = backStack.toRoute<VerifyEmailPage>()
            val registrationState =
                registrationViewModel.registrationUiState.collectAsStateWithLifecycle().value
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
                    registrationViewModel.onEvent(
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
                modifier = Modifier
            )
        }
    }
}