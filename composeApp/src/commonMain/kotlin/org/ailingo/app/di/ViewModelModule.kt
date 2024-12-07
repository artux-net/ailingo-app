package org.ailingo.app.di

import org.ailingo.app.features.chat.presentation.ChatViewModel
import org.ailingo.app.features.dictionary.main.presentation.DictionaryViewModel
import org.ailingo.app.features.login.presentation.LoginViewModel
import org.ailingo.app.features.registration.presentation.RegisterViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel() }
    viewModel { ChatViewModel() }
    viewModel { DictionaryViewModel(get()) }
}