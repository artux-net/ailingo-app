package org.ailingo.app.di

import org.ailingo.app.features.dictionary.historysearch.di.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory() }
}

