package org.ailingo.app.features.dictionary.history.di

import kotlinx.coroutines.Deferred
import org.ailingo.app.features.dictionary.history.domain.DictionaryRepository

expect class AppModule {
    val dictionaryRepository: Deferred<DictionaryRepository>
}
