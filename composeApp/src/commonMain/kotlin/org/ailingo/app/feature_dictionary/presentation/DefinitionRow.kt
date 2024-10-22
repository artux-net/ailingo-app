package org.ailingo.app.feature_dictionary.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Volume2
import org.ailingo.app.feature_dictionary.data.model.Def
import org.ailingo.app.feature_dictionary.presentation.utils.getPartOfSpeechLabel
import org.ailingo.app.feature_dictionary_examples.data.model.WordInfoItem
import org.ailingo.app.playSound


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefinitionRowInfo(definition: Def, responseForExamples: List<WordInfoItem>?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            definition.text,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.width(8.dp))
        //val fontFamilyForTranscription: FontFamily = fontFamilyResource(SharedRes.fonts.NotoSans.light)
       // Text("[" + definition.ts + "]", fontFamily = fontFamilyForTranscription, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(getPartOfSpeechLabel(definition.pos))
        val listOfAllAudio = responseForExamples?.flatMap { wordInfoItem ->
            wordInfoItem.phonetics
                .mapNotNull {
                    it.audio.takeIf { audio ->
                        audio?.isNotBlank() ?: false
                    }
                }
        }
        val firstNonEmptyAudio = listOfAllAudio?.firstOrNull()
        if (firstNonEmptyAudio != null) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                IconButton(onClick = {
                    playSound(firstNonEmptyAudio)
                }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = FeatherIcons.Volume2,
                        contentDescription = null
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))

    definition.tr.forEachIndexed { index, tr ->
        DefinitionEntry(index, tr)
    }
}
