package org.ailingo.app.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.ailingo.app.RootComponent
import org.ailingo.app.core.presentation.utils.DrawerItems
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AppDrawerContent(
    drawerState: DrawerState,
    root: RootComponent,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        DrawerItems.FreeMode,
        DrawerItems.Topics,
        DrawerItems.Dictionary,
        DrawerItems.Exit,
    )
    val scope = rememberCoroutineScope()
    LazyColumn(
        modifier = modifier
            .width(250.dp)
            .fillMaxHeight()
            .background(Color.White)
    ) {
        items(items) { item ->
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = Color.White
                ),
                modifier = Modifier.clickable {
                    when (item) {
                        DrawerItems.Dictionary -> {
                            scope.launch {
                                drawerState.close()
                            }
                            root.navigateToDictionaryScreen()
                        }

                        DrawerItems.Exit -> {
                            scope.launch {
                                drawerState.close()
                            }
                            root.navigateToLandingScreen()
                        }

                        DrawerItems.FreeMode -> {
                            scope.launch {
                                drawerState.close()
                            }
                            root.navigateToChatScreen()
                        }

                        DrawerItems.Topics -> {
                            scope.launch {
                                drawerState.close()
                            }
                            root.navigateToTopicsScreen()
                        }
                    }
                },
                leadingContent = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(
                            item.title
                        )
                    )
                },
                headlineContent = {
                    Text(text = stringResource(item.title))
                },
            )
        }
    }
}