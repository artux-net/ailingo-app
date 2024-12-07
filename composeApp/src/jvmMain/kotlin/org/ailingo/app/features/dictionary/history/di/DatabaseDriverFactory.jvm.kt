package org.ailingo.app.features.dictionary.history.di

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.ailingo.app.database.HistoryDictionaryDatabase


actual class DatabaseDriverFactory {
    actual suspend fun provideDbDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): SqlDriver {
        val driver = JdbcSqliteDriver(url = "jdbc:sqlite:dictionary_database.db")
            .also {
                HistoryDictionaryDatabase.Schema.create(driver = it).await()
            }
        return driver
    }
}