package org.ailingo.app.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.ailingo.app.AppDatabase
import org.ailingo.app.features.dictionary.historysearch.data.repository.DictionarySearchHistorySearchHistoryRepositoryImpl
import org.ailingo.app.features.dictionary.historysearch.di.DatabaseDriverFactory
import org.ailingo.app.features.dictionary.historysearch.domain.repository.DictionarySearchHistoryRepository
import org.ailingo.app.features.jwt.data.repository.TokenRepositoryImpl
import org.ailingo.app.features.jwt.domain.repository.TokenRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val databaseModule = module {
    single<Deferred<DictionarySearchHistoryRepository>>(named("dictionaryRepository")) {
        CoroutineScope(Dispatchers.Default).async {
            val driver = get<DatabaseDriverFactory>().provideDbDriver(
                AppDatabase.Schema
            )
            val db = AppDatabase(driver)
            DictionarySearchHistorySearchHistoryRepositoryImpl(db, get())
        }
    }
    single<Deferred<TokenRepository>>(named("tokenRepository")) {
        CoroutineScope(Dispatchers.Default).async {
            val driver = get<DatabaseDriverFactory>().provideDbDriver(
                AppDatabase.Schema
            )
            val db = AppDatabase(driver)
            TokenRepositoryImpl(db)
        }
    }
}