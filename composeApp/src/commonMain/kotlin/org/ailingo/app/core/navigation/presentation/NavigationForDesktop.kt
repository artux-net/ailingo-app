package org.ailingo.app.core.navigation.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import org.ailingo.app.ChatPage
import org.ailingo.app.DictionaryPage
import org.ailingo.app.LoginPage
import org.ailingo.app.ProfilePage
import org.ailingo.app.TopicsPage
import org.ailingo.app.core.presentation.model.DrawerItems
import org.ailingo.app.core.presentation.topappbar.TopAppBarCenter
import org.ailingo.app.core.presentation.topappbar.TopAppBarWithProfile
import org.ailingo.app.core.utils.windowinfo.info.WindowInfo
import org.ailingo.app.features.login.presentation.LoginScreenEvent
import org.ailingo.app.features.login.presentation.LoginViewModel
import org.ailingo.app.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun NavigationForDesktop(
    navController: NavHostController,
    isStandardCenterTopAppBarVisible: Boolean,
    isTopAppBarWithProfileVisible: Boolean,
    loginViewModel: LoginViewModel,
    currentDestination: NavDestination?,
    windowInfo: WindowInfo,
    contentNavHost: @Composable (padding: PaddingValues) -> Unit
) {
    val routesWithNavigationDrawer = listOf(
        ChatPage::class,
        TopicsPage::class,
        DictionaryPage::class,
        ProfilePage::class,
    )

    val isNavigationDrawerVisible = currentDestination?.let { dest ->
        routesWithNavigationDrawer.any { routeClass ->
            dest.hasRoute(routeClass)
        }
    } ?: false

    AppTheme {
        Scaffold(
            topBar = {
                if (isTopAppBarWithProfileVisible) {
                    TopAppBarWithProfile(loginViewModel = loginViewModel, windowInfo = windowInfo)
                } else {
                    if (isStandardCenterTopAppBarVisible) {
                        TopAppBarCenter()
                    }
                }
            }
        ) { innerPadding ->
            PermanentNavigationDrawer(
                modifier = Modifier,
                drawerContent = {
                    if (isNavigationDrawerVisible) {
                        val items = listOf(
                            DrawerItems.ChatMode,
                            DrawerItems.Topics,
                            DrawerItems.Dictionary,
                            DrawerItems.Profile,
                            DrawerItems.Exit,
                        )
                        val selectedItem = remember { mutableStateOf(items[0]) }

                        PermanentDrawerSheet(
                            modifier = Modifier.padding(top = 64.dp).width(350.dp).fillMaxHeight(),
                            drawerContainerColor = Color.White
                        ) {
                            items.forEach { item ->
                                if (item == DrawerItems.Exit) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                                NavigationDrawerItem(
                                    icon = { Icon(item.icon, contentDescription = null) },
                                    label = { Text(stringResource(item.title)) },
                                    selected = item == selectedItem.value,
                                    onClick = {
                                        selectedItem.value = item
                                        when (item) {
                                            DrawerItems.ChatMode -> navController.navigate(ChatPage)
                                            DrawerItems.Topics -> navController.navigate(TopicsPage)
                                            DrawerItems.Dictionary -> navController.navigate(
                                                DictionaryPage
                                            )

                                            DrawerItems.Profile -> navController.navigate(
                                                ProfilePage
                                            )

                                            DrawerItems.Exit -> {
                                                navController.navigate(LoginPage)
                                                loginViewModel.onEvent(LoginScreenEvent.OnBackToEmptyState)
                                            }
                                        }
                                    },
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                )
                            }
                        }
                    }
                }
            ) {
                contentNavHost(innerPadding)
            }
        }
    }
}