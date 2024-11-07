package org.ailingo.app.features.dictionary.main.presentation.utils

import androidx.compose.runtime.Composable

@Composable
fun getPartOfSpeechLabel(pos: String): String {
    return when (pos) {
        "noun" -> "сущ"
        "adjective" -> "прил"
        "verb" -> "гл"
        "foreign word" -> "иностр"
        else -> pos
    }
}
