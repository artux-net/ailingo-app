package org.ailingo.app.features.registration.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.ailingo.app.features.registration.utils.isValidEmail

class RegisterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState(isLoading = false))
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(onSuccess: () -> Unit) {
        val loginValid = _uiState.value.login.length in 4..16 && _uiState.value.login.isNotBlank()
        val passwordValid = _uiState.value.password.length in 8..24 && _uiState.value.password.isNotBlank()
        val emailValid = isValidEmail(_uiState.value.email) && _uiState.value.email.isNotBlank()
        val nameValid = _uiState.value.name.isNotBlank()

        _uiState.update {
            it.copy(
                isLoginValid = loginValid,
                isPasswordValid = passwordValid,
                isEmailValid = emailValid,
                isNameValid = nameValid
            )
        }

        if (loginValid && passwordValid && emailValid && nameValid) {
            onSuccess()
        }
    }

    fun onLoadingChange(isLoading: Boolean) = _uiState.update { it.copy(isLoading = isLoading) }
    fun onLoginChange(login: String) = _uiState.update { it.copy(login = login) }
    fun onPasswordChange(password: String) = _uiState.update { it.copy(password = password) }
    fun onEmailChange(email: String) = _uiState.update { it.copy(email = email) }
    fun onNameChange(name: String) = _uiState.update { it.copy(name = name) }
}
