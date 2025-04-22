package org.ailingo.app.di

import org.ailingo.app.features.chat.presentation.ChatViewModel
import org.ailingo.app.features.dictionary.main.presentation.DictionaryViewModel
import org.ailingo.app.features.favouritewords.presentation.FavouriteWordsViewModel
import org.ailingo.app.features.login.presentation.LoginViewModel
import org.ailingo.app.features.profileupdate.presentation.ProfileUpdateViewModel
import org.ailingo.app.features.registration.presentation.RegisterUserViewModel
import org.ailingo.app.features.registration.presentation.email_verification.OtpViewModel
import org.ailingo.app.features.topics.presentation.TopicViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterUserViewModel(get(), get()) }
    viewModel { OtpViewModel() }
    factory { (topicName: String) -> ChatViewModel(get(), topicName) }
    factory { (word: String) ->
        DictionaryViewModel(
            get(named("dictionaryRepository")),
            get(),
            get(),
            get(),
            word
        )
    }
    viewModel { TopicViewModel(get()) }
    viewModel { ProfileUpdateViewModel(get()) }
    viewModel { FavouriteWordsViewModel(get()) }
}