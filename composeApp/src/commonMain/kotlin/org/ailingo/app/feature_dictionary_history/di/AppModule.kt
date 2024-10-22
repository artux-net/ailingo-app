package org.ailingo.app.feature_dictionary_history.di

import kotlinx.coroutines.Deferred
import org.ailingo.app.feature_dictionary_history.domain.DictionaryRepository

expect class AppModule {
    val dictionaryRepository: Deferred<DictionaryRepository>
}