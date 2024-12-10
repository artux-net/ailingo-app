package org.ailingo.app.core.navigation.model

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.dictionary
import ailingo.composeapp.generated.resources.free_mode
import ailingo.composeapp.generated.resources.profile
import ailingo.composeapp.generated.resources.topics
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Topic
import androidx.compose.ui.graphics.vector.ImageVector
import org.ailingo.app.ChatPage
import org.ailingo.app.DictionaryPage
import org.ailingo.app.ProfilePage
import org.ailingo.app.TopicsPage
import org.jetbrains.compose.resources.StringResource

sealed class BottomNavItem(
    val title: StringResource,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Any
) {
    data object ChatMode : BottomNavItem(
        title = Res.string.free_mode,
        selectedIcon = Icons.AutoMirrored.Filled.Message,
        unselectedIcon = Icons.AutoMirrored.Outlined.Message,
        route = ChatPage
    )

    data object Topics : BottomNavItem(
        title = Res.string.topics,
        selectedIcon = Icons.Filled.Topic,
        unselectedIcon = Icons.Outlined.Topic,
        route = TopicsPage
    )

    data object Dictionary : BottomNavItem(
        title = Res.string.dictionary,
        selectedIcon = Icons.Filled.Book,
        unselectedIcon = Icons.Outlined.Book,
        route = DictionaryPage
    )

    data object Profile : BottomNavItem(
        title = Res.string.profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        route = ProfilePage
    )
}