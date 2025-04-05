package org.ailingo.app.core.presentation

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private const val PADDING_PERCENTAGE_OUTER_CIRCLE = 0.15f
private const val PADDING_PERCENTAGE_INNER_CIRCLE = 0.3f
private const val POSITION_START_OFFSET_OUTER_CIRCLE = 90f
private const val POSITION_START_OFFSET_INNER_CIRCLE = 135f

@Composable
fun LoadingScreen(
    loadingText: String? = null,
    contentAlignment: Alignment = Alignment.Center,
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinity_loading_animation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000)
        ), label = "rotation_loading_animation"
    )

    var width by remember {
        mutableIntStateOf(0)
    }

    Box(modifier = modifier, contentAlignment = contentAlignment) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .onSizeChanged {
                        width = it.width
                    },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 1.dp,
                    modifier = Modifier
                        .size(40.dp)
                        .graphicsLayer {
                            rotationZ = rotation
                        }
                )
                CircularProgressIndicator(
                    strokeWidth = 1.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            with(LocalDensity.current) {
                                (width * PADDING_PERCENTAGE_INNER_CIRCLE).toDp()
                            }
                        )
                        .graphicsLayer {
                            rotationZ = rotation + POSITION_START_OFFSET_INNER_CIRCLE
                        }
                )
                CircularProgressIndicator(
                    strokeWidth = 1.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            with(LocalDensity.current) {
                                (width * PADDING_PERCENTAGE_OUTER_CIRCLE).toDp()
                            }
                        )
                        .graphicsLayer {
                            rotationZ = rotation + POSITION_START_OFFSET_OUTER_CIRCLE
                        }
                )
            }
            if (loadingText != null) {
                Text(loadingText, textAlign = textAlign)
            }
        }
    }
}


@Composable
fun SmallLoadingIndicator(modifier: Modifier = Modifier, text: String? = null) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinity_loading_animation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000)
        ), label = "rotation_loading_animation"
    )

    var width by remember {
        mutableIntStateOf(0)
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = modifier
                    .size(20.dp)
                    .onSizeChanged {
                        width = it.width
                    },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 1.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationZ = rotation
                        }
                )
                CircularProgressIndicator(
                    strokeWidth = 1.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            with(LocalDensity.current) {
                                (width * PADDING_PERCENTAGE_INNER_CIRCLE).toDp()
                            }
                        )
                        .graphicsLayer {
                            rotationZ = rotation + POSITION_START_OFFSET_INNER_CIRCLE
                        }
                )
                CircularProgressIndicator(
                    strokeWidth = 1.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            with(LocalDensity.current) {
                                (width * PADDING_PERCENTAGE_OUTER_CIRCLE).toDp()
                            }
                        )
                        .graphicsLayer {
                            rotationZ = rotation + POSITION_START_OFFSET_OUTER_CIRCLE
                        }
                )
            }
            if (text != null) {
                Text(text)
            }
        }
    }
}