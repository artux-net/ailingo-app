package org.ailingo.app.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.ailingo.app.database.HistoryDictionaryDatabase
import org.ailingo.app.features.dictionary.history.data.DictionaryRepositoryImpl
import org.ailingo.app.features.dictionary.history.di.DatabaseDriverFactory
import org.ailingo.app.features.dictionary.history.domain.DictionaryRepository
import org.koin.dsl.module

val databaseModule = module {
    single<Deferred<DictionaryRepository>> {
        CoroutineScope(Dispatchers.Default).async {
            val driver = get<DatabaseDriverFactory>().provideDbDriver(
                HistoryDictionaryDatabase.Schema
            )
            val db = HistoryDictionaryDatabase(driver)
            DictionaryRepositoryImpl(db)
        }
    }
}