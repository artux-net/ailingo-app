package org.ailingo.app.core.navigation.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.dictionary
import ailingo.composeapp.generated.resources.free_mode
import ailingo.composeapp.generated.resources.profile
import ailingo.composeapp.generated.resources.topics
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Topic
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import org.ailingo.app.ChatPage
import org.ailingo.app.DictionaryPage
import org.ailingo.app.LandingPage
import org.ailingo.app.LoginPage
import org.ailingo.app.ProfilePage
import org.ailingo.app.RegisterPage
import org.ailingo.app.ResetPasswordPage
import org.ailingo.app.SelectPage
import org.ailingo.app.TopicsPage
import org.ailingo.app.UploadAvatarPage
import org.ailingo.app.core.navigation.model.BottomNavigationItem
import org.ailingo.app.core.presentation.TopAppBarCenter
import org.ailingo.app.core.presentation.TopAppBarWithProfile
import org.ailingo.app.features.login.presentation.LoginViewModel
import org.ailingo.app.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun NavigationForMobile(
    navController: NavHostController,
    currentDestination: NavDestination?,
    showTopAppCenter: Boolean?,
    showTopAppBarWithProfile: Boolean?,
    loginViewModel: LoginViewModel,
    contentNavHost: @Composable (padding: PaddingValues) -> Unit
) {
    val routesWithoutBottomBar = listOf(
        LoginPage::class,
        LandingPage::class,
        RegisterPage::class,
        UploadAvatarPage::class,
        SelectPage::class,
        ResetPasswordPage::class
    )

    val showBottomBar = currentDestination?.let {
        !routesWithoutBottomBar.any { routeClass ->
            it.hasRoute(routeClass)
        }
    }

    val items = listOf(
        BottomNavigationItem(
            title = stringResource(Res.string.free_mode),
            selectedIcon = Icons.AutoMirrored.Filled.Message,
            unselectedIcon = Icons.AutoMirrored.Outlined.Message,
            route = ChatPage
        ),
        BottomNavigationItem(
            title = stringResource(Res.string.topics),
            selectedIcon = Icons.Filled.Topic,
            unselectedIcon = Icons.Outlined.Topic,
            route = TopicsPage
        ),
        BottomNavigationItem(
            title = stringResource(Res.string.dictionary),
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
            route = DictionaryPage
        ),
        BottomNavigationItem(
            title = stringResource(Res.string.profile),
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            route = ProfilePage
        ),
    )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    AppTheme {
        Scaffold(
            topBar = {
                if (showTopAppBarWithProfile == true) {
                    TopAppBarWithProfile(loginViewModel = loginViewModel)
                } else {
                    if (showTopAppCenter == true) {
                        TopAppBarCenter()
                    }
                }
            },
            bottomBar = {
                if (showBottomBar == true) {
                    NavigationBar {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                label = {
                                    Text(item.title)
                                },
                                onClick = {
                                    navController.navigate(item.route) {
                                        navController.graph.findStartDestination().route?.let {
                                            popUpTo(it) {
                                                saveState = true
                                            }
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    selectedItemIndex = index
                                },
                                icon = {
                                    if (selectedItemIndex == index) {
                                        Icon(
                                            imageVector = item.selectedIcon,
                                            contentDescription = item.title
                                        )
                                    } else {
                                        Icon(
                                            imageVector = item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            contentNavHost(innerPadding)
        }
    }
}