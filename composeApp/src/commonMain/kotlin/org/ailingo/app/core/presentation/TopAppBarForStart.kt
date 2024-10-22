package org.ailingo.app.core.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.ailingologowithoutbackground
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun TopAppBarForStart() {
    CenterAlignedTopAppBar(
        title = {
            Icon(
                painter = painterResource(Res.drawable.ailingologowithoutbackground),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.height(40.dp),
            )
        }
    )
}
