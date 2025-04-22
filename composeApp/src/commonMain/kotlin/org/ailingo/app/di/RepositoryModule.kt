package org.ailingo.app.di

import org.ailingo.app.features.chat.data.repository.ChatRepositoryImpl
import org.ailingo.app.features.chat.domain.repository.ChatRepository
import org.ailingo.app.features.dictionary.main.data.repository.DictionaryRepositoryImpl
import org.ailingo.app.features.dictionary.main.domain.repository.DictionaryRepository
import org.ailingo.app.features.dictionary.predictor.data.repository.PredictWordsRepositoryImpl
import org.ailingo.app.features.dictionary.predictor.domain.repository.PredictWordsRepository
import org.ailingo.app.features.favouritewords.data.repository.FavouriteWordsRepositoryImpl
import org.ailingo.app.features.favouritewords.domain.repository.FavouriteWordsRepository
import org.ailingo.app.features.login.data.repository.LoginRepositoryImpl
import org.ailingo.app.features.login.domain.repository.LoginRepository
import org.ailingo.app.features.profileupdate.data.repository.ProfileUpdateRepositoryImpl
import org.ailingo.app.features.profileupdate.domain.repository.ProfileUpdateRepository
import org.ailingo.app.features.registration.data.repository.RegisterRepositoryImpl
import org.ailingo.app.features.registration.data.repository.VerifyEmailRepositoryImpl
import org.ailingo.app.features.registration.domain.repository.RegisterRepository
import org.ailingo.app.features.registration.domain.repository.VerifyEmailRepository
import org.ailingo.app.features.topics.data.repository.TopicRepositoryImpl
import org.ailingo.app.features.topics.domain.repository.TopicRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<RegisterRepository> {
        RegisterRepositoryImpl(get(), get())
    }
    single<VerifyEmailRepository> {
        VerifyEmailRepositoryImpl(get(), get())
    }
    single<TopicRepository> {
        TopicRepositoryImpl(get(), get())
    }
    single<LoginRepository> {
        LoginRepositoryImpl(get(), get(), get(named("tokenRepository")))
    }
    single<FavouriteWordsRepository> {
        FavouriteWordsRepositoryImpl(get(), get())
    }
    single<PredictWordsRepository> {
        PredictWordsRepositoryImpl(get(), get())
    }
    single<DictionaryRepository> {
        DictionaryRepositoryImpl(get(), get())
    }
    single<ProfileUpdateRepository> {
        ProfileUpdateRepositoryImpl(get(), get())
    }
    single<ChatRepository> {
        ChatRepositoryImpl(get(), get())
    }
}