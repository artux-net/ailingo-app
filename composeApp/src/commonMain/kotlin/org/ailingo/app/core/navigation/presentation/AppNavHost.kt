package org.ailingo.app.core.navigation.presentation

import ChatPage
import DictionaryPage
import FavouriteWordsPage
import GetStartedPage
import LoginPage
import ProfilePage
import ProfileUpdatePage
import RegistrationPage
import TopicsPage
import VerifyEmailPage
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import org.ailingo.app.core.utils.voice.VoiceToTextParser
import org.ailingo.app.core.utils.windowinfo.info.WindowInfo
import org.ailingo.app.features.chat.presentation.ChatScreen
import org.ailingo.app.features.chat.presentation.ChatViewModel
import org.ailingo.app.features.dictionary.main.presentation.DictionaryEvents
import org.ailingo.app.features.dictionary.main.presentation.DictionaryScreen
import org.ailingo.app.features.dictionary.main.presentation.DictionaryViewModel
import org.ailingo.app.features.favouritewords.presentation.FavouriteScreen
import org.ailingo.app.features.favouritewords.presentation.FavouriteWordsViewModel
import org.ailingo.app.features.introduction.presentation.GetStartedScreen
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
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

data class ComposableAnimationSpecs(
    val enter: EnterTransition,
    val exit: ExitTransition,
    val popEnter: EnterTransition,
    val popExit: ExitTransition
)

fun defaultComposableAnimation(): ComposableAnimationSpecs {
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
            enterTransition = { defaultComposableAnimation().enter },
            exitTransition = { defaultComposableAnimation().exit },
            popEnterTransition = { defaultComposableAnimation().popEnter },
            popExitTransition = { defaultComposableAnimation().popExit }
        ) {
            LoginScreen(
                loginState = loginState,
                onNavigateToHomeScreen = {
                    navController.navigate(TopicsPage) {
                        popUpTo(0)
                    }
                },
                onNavigateToRegisterScreen = {
                    navController.navigate(RegistrationPage)
                },
                onEvent = { event ->
                    loginViewModel.onEvent(event)
                }
            )
        }
        composable<ChatPage>(
            enterTransition = { defaultComposableAnimation().enter },
            exitTransition = { defaultComposableAnimation().exit },
            popEnterTransition = { defaultComposableAnimation().popEnter },
            popExitTransition = { defaultComposableAnimation().popExit }
        ) { backStack ->
            val args = backStack.toRoute<ChatPage>()
            val chatViewModel: ChatViewModel = koinViewModel { parametersOf(args.topicName) }
            val uiState = chatViewModel.chatState.collectAsStateWithLifecycle().value
            val messagesState = chatViewModel.messages.collectAsStateWithLifecycle().value

            ChatScreen(
                args.topicName,
                args.topicImage,
                uiState = uiState,
                messagesState = messagesState,
                onEvent = {
                    chatViewModel.onEvent(it)
                }
            )
        }
        composable<RegistrationPage>(
            enterTransition = { defaultComposableAnimation().enter },
            exitTransition = { defaultComposableAnimation().exit },
            popEnterTransition = { defaultComposableAnimation().popEnter },
            popExitTransition = { defaultComposableAnimation().popExit }
        ) {
            val pendingRegistrationState =
                registrationViewModel.pendingRegistrationUiState.collectAsStateWithLifecycle().value
            RegistrationScreen(
                onNavigateToLoginPage = {
                    navController.navigate(LoginPage)
                },
                onNavigateToVerifyEmail = { email, password ->
                    navController.navigate(VerifyEmailPage(email, password))
                },
                pendingRegistrationState = pendingRegistrationState,
                onEvent = { event ->
                    registrationViewModel.onEvent(event)
                }
            )
        }
        composable<GetStartedPage>(
            enterTransition = { defaultComposableAnimation().enter },
            exitTransition = { defaultComposableAnimation().exit },
            popEnterTransition = { defaultComposableAnimation().popEnter },
            popExitTransition = { defaultComposableAnimation().popExit }
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
            enterTransition = { defaultComposableAnimation().enter },
            exitTransition = { defaultComposableAnimation().exit },
            popEnterTransition = { defaultComposableAnimation().popEnter },
            popExitTransition = { defaultComposableAnimation().popExit }
        ) {
            val topicsViewModel: TopicViewModel = koinViewModel<TopicViewModel>()
            val topicsUiState = topicsViewModel.topicState.collectAsStateWithLifecycle().value
            TopicsScreen(
                windowInfo = windowInfo,
                topicsUiState = topicsUiState,
                onTopicClick = { topicName, topicImage ->
                    navController.navigate(ChatPage(topicName = topicName, topicImage = topicImage))
                }
            )
        }
        composable<DictionaryPage>(
            enterTransition = { defaultComposableAnimation().enter },
            exitTransition = { defaultComposableAnimation().exit },
            popEnterTransition = { defaultComposableAnimation().popEnter },
            popExitTransition = { defaultComposableAnimation().popExit }
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<DictionaryPage>()
            val dictionaryViewModel: DictionaryViewModel = koinViewModel<DictionaryViewModel>()
            LaunchedEffect(true) {
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
                onEvent = { event ->
                    dictionaryViewModel.onEvent(event)
                }
            )
        }
        composable<ProfilePage>(
            enterTransition = { defaultComposableAnimation().enter },
            exitTransition = { defaultComposableAnimation().exit },
            popEnterTransition = { defaultComposableAnimation().popEnter },
            popExitTransition = { defaultComposableAnimation().popExit }
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
            enterTransition = { defaultComposableAnimation().enter },
            exitTransition = { defaultComposableAnimation().exit },
            popEnterTransition = { defaultComposableAnimation().popEnter },
            popExitTransition = { defaultComposableAnimation().popExit }
        ) {
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
        composable<FavouriteWordsPage>(
            enterTransition = { defaultComposableAnimation().enter },
            exitTransition = { defaultComposableAnimation().exit },
            popEnterTransition = { defaultComposableAnimation().popEnter },
            popExitTransition = { defaultComposableAnimation().popExit }
        ) {
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
        composable<VerifyEmailPage>(
            enterTransition = { defaultComposableAnimation().enter },
            exitTransition = { defaultComposableAnimation().exit },
            popEnterTransition = { defaultComposableAnimation().popEnter },
            popExitTransition = { defaultComposableAnimation().popExit }
        ) { backStack ->
            val args = backStack.toRoute<VerifyEmailPage>()
            val registrationState = registrationViewModel.registrationUiState.collectAsStateWithLifecycle().value
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
                modifier = Modifier,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}