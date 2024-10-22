package org.ailingo.app

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import kotlinx.coroutines.Deferred
import kotlinx.serialization.Serializable
import org.ailingo.app.feature_chat.presentation.ChatScreenComponent
import org.ailingo.app.feature_dictionary.presentation.DictionaryScreenComponent
import org.ailingo.app.feature_dictionary_history.domain.DictionaryRepository
import org.ailingo.app.feature_get_started.presentation.GetStartedScreenComponent
import org.ailingo.app.feature_landing.presentation.LandingScreenComponent
import org.ailingo.app.feature_login.presentation.LoginScreenComponent
import org.ailingo.app.feature_register.presentation.RegisterScreenComponent
import org.ailingo.app.feature_reset_password.presentation.ResetPasswordScreenComponent
import org.ailingo.app.feature_topics.presentation.TopicsScreenComponent
import org.ailingo.app.feature_upload_avatar.UploadAvatarComponent

class RootComponent(
    componentContext: ComponentContext,
    private val historyDictionaryRepository: Deferred<DictionaryRepository>
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()

    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = if (getPlatformName() == "Android") Configuration.LoginScreen else Configuration.LandingScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun navigateTo(configuration: Configuration) {
        navigation.navigate(
            transformer = { stack ->
                if (stack.contains(configuration)) {
                    val updatedStack = stack.filter { it != configuration } + configuration
                    updatedStack
                } else {
                    stack + configuration
                }
            }
        )
    }

    fun navigateToDictionaryScreen() {
        navigateTo(Configuration.DictionaryScreen)
    }

    fun navigateToTopicsScreen() {
        navigateTo(Configuration.TopicsScreen)
    }

    fun navigateToLandingScreen() {
        navigation.navigate(
            transformer = {
                listOf(Configuration.LandingScreen)
            }
        )
    }

    fun navigateToChatScreen() {
        navigateTo(Configuration.ChatScreen)
    }

    fun navigateToRegisterScreen() {
        navigateTo(Configuration.RegisterScreen)
    }


    private fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.LandingScreen -> {
                Child.LandingScreen(
                    LandingScreenComponent(
                        componentContext = context,
                        onNavigateToLoginScreen = {
                            navigateTo(Configuration.LoginScreen)
                        }
                    )
                )
            }

            Configuration.LoginScreen -> {
                Child.LoginScreen(
                    LoginScreenComponent(
                        componentContext = context,
                        onNavigateToChatScreen = {
                            navigateTo(Configuration.ChatScreen)
                        },
                        onNavigateToResetPasswordScreen = {
                            navigateTo(Configuration.ResetPasswordScreen)
                        },
                        onNavigateToRegisterScreen = {
                            navigateTo(Configuration.RegisterScreen)
                        }
                    )
                )
            }

            Configuration.ChatScreen -> {
                Child.ChatScreen(
                    ChatScreenComponent(
                        componentContext = context
                    )
                )
            }

            Configuration.ResetPasswordScreen -> {
                Child.ResetPasswordScreen(
                    ResetPasswordScreenComponent(
                        componentContext = context,
                        onNavigateGetStartedScreen = {
                            navigateTo(Configuration.GetStartedScreen)
                        }
                    )
                )
            }

            Configuration.GetStartedScreen -> {
                Child.GetStartedScreen(
                    GetStartedScreenComponent(
                        componentContext = context,
                        onNavigateToLoginScreen = {
                            navigateTo(Configuration.LoginScreen)
                        },
                        onNavigateToRegisterScreen = {
                            navigateTo(Configuration.RegisterScreen)
                        }
                    )
                )
            }

            Configuration.RegisterScreen -> {
                Child.RegisterScreen(
                    RegisterScreenComponent(
                        componentContext = context,
                        onNavigateToUploadAvatarScreen = { login, password, email, name, savedPhoto ->
                            navigateTo(
                                Configuration.UploadAvatarScreen(
                                    login = login,
                                    password = password,
                                    email = email,
                                    name = name,
                                    savedPhoto = savedPhoto
                                )
                            )
                        },
                        onNavigateToLoginScreen = {
                            navigateTo(Configuration.LoginScreen)
                        }
                    )
                )
            }

            is Configuration.UploadAvatarScreen -> {
                Child.UploadAvatarScreen(
                    UploadAvatarComponent(
                        componentContext = context,
                        onNavigateToChatScreen = {
                            navigateTo(Configuration.ChatScreen)
                        },
                        onNavigateToRegisterScreen = {
                            navigateTo(Configuration.RegisterScreen)
                        },
                        login = configuration.login,
                        password = configuration.password,
                        email = configuration.email,
                        name = configuration.name
                    )
                )
            }

            Configuration.DictionaryScreen -> {
                Child.DictionaryScreen(
                    DictionaryScreenComponent(
                        componentContext = context,
                        historyDictionaryRepository = historyDictionaryRepository
                    )
                )
            }
            Configuration.TopicsScreen -> {
                Child.TopicsScreen(
                    TopicsScreenComponent(
                        componentContext = context
                    )
                )
            }
        }
    }

    sealed class Child {
        data class LandingScreen(val component: LandingScreenComponent) : Child()
        data class LoginScreen(val component: LoginScreenComponent) : Child()
        data class ChatScreen(val component: ChatScreenComponent) : Child()
        data class ResetPasswordScreen(val component: ResetPasswordScreenComponent) : Child()
        data class GetStartedScreen(val component: GetStartedScreenComponent) : Child()
        data class RegisterScreen(val component: RegisterScreenComponent) : Child()
        data class UploadAvatarScreen(val component: UploadAvatarComponent) : Child()
        data class TopicsScreen(val component: TopicsScreenComponent) : Child()
        data class DictionaryScreen(val component: DictionaryScreenComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object LandingScreen : Configuration()

        @Serializable
        data object GetStartedScreen : Configuration()

        @Serializable
        data object LoginScreen : Configuration()

        @Serializable
        data object RegisterScreen : Configuration()

        @Serializable
        data object ResetPasswordScreen : Configuration()

        @Serializable
        data object ChatScreen : Configuration()

        @Serializable
        data class UploadAvatarScreen(
            val login: String,
            val password: String,
            val email: String,
            val name: String,
            val savedPhoto: String
        ) : Configuration()

        @Serializable
        data object TopicsScreen : Configuration()

        @Serializable
        data object DictionaryScreen : Configuration()
    }
}
