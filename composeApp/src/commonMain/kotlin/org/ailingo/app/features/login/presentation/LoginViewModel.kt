package org.ailingo.app.features.login.presentation

import AiLingo.composeApp.BuildConfig.API_ENDPOINT_USER
import AiLingo.composeApp.BuildConfig.BASE_URL
import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.request_failed
import ailingo.composeapp.generated.resources.wrong_login_or_password
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ailingo.app.core.utils.auth.basicAuthHeader
import org.ailingo.app.di.NetworkErrorMapper
import org.jetbrains.compose.resources.getString

class LoginViewModel(
    private val httpClient: HttpClient,
    private val errorMapper: NetworkErrorMapper
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    var login by mutableStateOf("")
    var password by mutableStateOf("")

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
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
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            try {
                val response = httpClient.get("$BASE_URL$API_ENDPOINT_USER/info") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    header(HttpHeaders.Authorization, basicAuthHeader(login, password))
                }
                _loginState.value = when {
                    response.status.isSuccess() -> {
                        val body = response.body<LoginUiState.Success>()
                        LoginUiState.Success(
                            id = body.id,
                            login = body.login,
                            name = body.name,
                            email = body.email,
                            avatar = body.avatar,
                            xp = body.xp,
                            coins = body.coins,
                            streak = body.streak,
                            registration = body.registration,
                            lastLoginAt = body.lastLoginAt
                        )
                    }

                    else -> {
                        if (response.status.value == 401) {
                            LoginUiState.Error(getString(Res.string.wrong_login_or_password))
                        } else {
                            LoginUiState.Error(
                                getString(
                                    Res.string.request_failed,
                                    { response.status.value }
                                )
                            )
                        }
                    }
                }
            } catch (e: Throwable) {
                _loginState.update { LoginUiState.Error(errorMapper.mapError(e)) }
            }
        }
    }
}