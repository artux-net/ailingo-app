package org.ailingo.app.features.dictionary.historysearch.data.mapper

import org.ailingo.app.HistoryDictionaryEntity
import org.ailingo.app.features.dictionary.historysearch.data.model.DictionarySearchHistory

fun HistoryDictionaryEntity.toHistoryDictionary(): DictionarySearchHistory {
    return DictionarySearchHistory(
        id = id,
        text = text
    )
}
