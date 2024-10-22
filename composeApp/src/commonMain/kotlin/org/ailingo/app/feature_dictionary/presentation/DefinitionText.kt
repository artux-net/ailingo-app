package org.ailingo.app.feature_dictionary.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun DefinitionAndSynonymText(text: String?, gen: String?) {
    Row(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 4.dp)
    ) {
        text?.let {
            Text(it, color = Color.Black)
        }
        Spacer(modifier = Modifier.width(4.dp))
        gen?.let { Text(it, color = Color.DarkGray) }
    }
}