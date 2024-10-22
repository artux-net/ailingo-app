package org.ailingo.app.feature_dictionary.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ailingo.app.feature_dictionary.data.model.Tr
import org.ailingo.app.theme.ColorForMainTextDictionary
import org.ailingo.app.theme.ColorForSynonymsDictionary


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DefinitionEntry(index: Int, tr: Tr) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = (index + 1).toString(),
            modifier = Modifier.widthIn(min = 22.dp)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = ColorForMainTextDictionary)
            ) {
                DefinitionAndSynonymText(tr.text, tr.gen)
            }
            tr.syn?.forEach { syn ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = ColorForSynonymsDictionary)
                ) {
                    DefinitionAndSynonymText(syn.text, syn.gen)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
    FlowRow {
        Spacer(modifier = Modifier.width(34.dp))
        val meanText = tr.mean?.joinToString(", ") { it.text }
        if (meanText?.isNotBlank() == true) {
            Text(meanText, color = Color.DarkGray, fontSize = 14.sp)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}
