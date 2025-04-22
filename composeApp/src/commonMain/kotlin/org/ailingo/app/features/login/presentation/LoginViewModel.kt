package org.ailingo.app.features.login.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ailingo.app.features.login.domain.repository.LoginRepository

class LoginViewModel(
    private val loginRepository: LoginRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    var login by mutableStateOf("")
    var password by mutableStateOf("")

    init {
        checkUserAuth()
    }

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            LoginScreenEvent.OnBackToEmptyState -> {
                _loginState.value = LoginUiState.Unauthenticated
                viewModelScope.launch {
                    loginRepository.backToEmptyState()
                }
            }

            is LoginScreenEvent.OnLoginUser -> {
                loginUser(event.login, event.password)
            }

            LoginScreenEvent.RefreshUserInfo -> refreshUserInfo()
        }
    }

    private fun refreshUserInfo() {
        viewModelScope.launch {
            loginRepository.autoLogin().collect { state->
                _loginState.update { state }
            }
        }
    }

    private fun checkUserAuth() {
        viewModelScope.launch {
            loginRepository.autoLogin().collect { state->
                _loginState.update { state }
            }
        }
    }

    private fun loginUser(
        login: String,
        password: String
    ) {
        viewModelScope.launch {
            loginRepository.loginUser(login, password).collect { state->
                _loginState.update { state }
            }
        }
    }
}