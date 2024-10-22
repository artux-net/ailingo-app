package org.ailingo.app.feature_dictionary_history.data

import org.ailingo.app.database.HistoryDictionaryEntity
import org.ailingo.app.feature_dictionary_history.domain.HistoryDictionary

fun HistoryDictionaryEntity.toHistoryDictionary(): HistoryDictionary {
    return HistoryDictionary(
        id = id,
        text = text
    )
}