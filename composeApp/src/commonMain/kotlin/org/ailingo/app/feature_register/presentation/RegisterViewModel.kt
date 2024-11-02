package org.ailingo.app.feature_register.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    val login = mutableStateOf("")
    val password = mutableStateOf("")
    val email = mutableStateOf("")
    val name = mutableStateOf("")
}