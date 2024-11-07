package org.ailingo.app.features.topics.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.ailingo.app.features.topics.data.Topic
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopicCard(topic: Topic, modifier: Modifier = Modifier) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f), Color.Transparent)
    )

    var heightOfImage by remember {
        mutableStateOf(0.dp)
    }

    var widthOfImage by remember {
        mutableStateOf(0.dp)
    }

    val density = LocalDensity.current

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = modifier.aspectRatio(1f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.aspectRatio(1f)
        ) {
            Image(
                painter = painterResource(topic.image),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.aspectRatio(1f)
                    .onGloballyPositioned {
                        heightOfImage = with(density) {
                            it.size.height.toDp()
                        }
                        widthOfImage = with(density) {
                            it.size.width.toDp()
                        }
                    }
            )
            Box(
                modifier = Modifier.height(heightOfImage).width(widthOfImage).background(gradient)
            )
            Text(
                text = stringResource(topic.title),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
