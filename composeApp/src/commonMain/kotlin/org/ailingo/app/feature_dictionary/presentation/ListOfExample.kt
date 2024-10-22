package org.ailingo.app.feature_dictionary.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ailingo.app.feature_dictionary.presentation.utils.HighlightedText

@Composable
fun ListOfExample(listOfExamples: List<String>?, textFieldValue: String) {
    listOfExamples?.forEachIndexed { index, example ->
        val newIndex = index + 1
        Column {
            Row {
                Text(
                    text = newIndex.toString(),
                    modifier = Modifier.widthIn(min = 22.dp)
                )
                HighlightedText(text = example, searchQuery = textFieldValue)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}