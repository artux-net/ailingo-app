package org.ailingo.app.feature_login.presentation

import AiLingo.composeApp.BuildConfig.API_ENDPOINT_USER
import AiLingo.composeApp.BuildConfig.BASE_URL
import com.arkivanov.decompose.ComponentContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.ailingo.app.core.helper_auth.auth.basicAuthHeader
import org.ailingo.app.core.util.componentCoroutineScope

class LoginScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToChatScreen: () -> Unit,
    private val onNavigateToResetPasswordScreen: () -> Unit,
    private val onNavigateToRegisterScreen: () -> Unit
) : ComponentContext by componentContext {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val coroutineScope = componentCoroutineScope()

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            LoginScreenEvent.OnNavigateToChatScreen -> onNavigateToChatScreen()
            LoginScreenEvent.OnNavigateToResetPasswordScreen -> onNavigateToResetPasswordScreen()
            LoginScreenEvent.OnNavigateToRegisterScreen -> onNavigateToRegisterScreen()
            LoginScreenEvent.OnBackToEmptyState -> {
                _loginState.value = LoginUiState.Empty
            }
            is LoginScreenEvent.OnLoginUser -> {
                loginUser(event.login, event.password)
            }
        }
    }

    private fun loginUser(
        login: String,
        password: String
    ) {
        coroutineScope.launch {
            val httpClient = HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
            }
            _loginState.value = LoginUiState.Loading
            try {
                val response = httpClient.get("$BASE_URL$API_ENDPOINT_USER/info") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    header(HttpHeaders.Authorization, basicAuthHeader(login, password))
                }
                _loginState.value = when {
                    response.status.isSuccess() -> {
                        val body = response.body<LoginUiState.Success>()
                        println(body)
                        LoginUiState.Success(
                            body.id,
                            body.login,
                            body.avatar,
                            body.xp,
                            body.coins,
                            body.streak,
                            body.registration,
                            body.lastLoginAt
                        )
                    } else -> {
                        if (response.status.value == 401) {
                            LoginUiState.Error("Wrong login or password")
                        } else {
                            LoginUiState.Error("Request failed with ${response.status.value}")
                        }
                    }
                }
            } catch (e: Exception) {
                _loginState.update {
                    LoginUiState.Error(e.message.toString())
                }
            } finally {
                httpClient.close()
            }
        }
    }
}