package org.ailingo.app.features.profile.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.change_user_data
import ailingo.composeapp.generated.resources.coins
import ailingo.composeapp.generated.resources.defaultProfilePhoto
import ailingo.composeapp.generated.resources.exit
import ailingo.composeapp.generated.resources.icon_experience
import ailingo.composeapp.generated.resources.profile_background
import ailingo.composeapp.generated.resources.streak
import ailingo.composeapp.generated.resources.xp
import ailingo.composeapp.generated.resources.your_statistics
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.ailingo.app.core.presentation.ErrorScreen
import org.ailingo.app.core.presentation.LoadingScreen
import org.ailingo.app.features.login.data.model.User
import org.ailingo.app.features.login.presentation.LoginUiState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileScreen(
    loginState: LoginUiState,
    onExit: () -> Unit,
    onNavigateProfileChange: (
        name: String,
        email: String,
        avatar: String?
    ) -> Unit
) {

    when (loginState) {
        is LoginUiState.Error -> {
            ErrorScreen(
                errorMessage = loginState.message,
                modifier = Modifier.fillMaxSize()
            )
        }

        LoginUiState.Loading -> {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }

        is LoginUiState.Success -> {
            ProfileContent(
                modifier = Modifier.fillMaxSize(),
                user = loginState.user,
                onExit = onExit,
                onNavigateProfileChange = {
                    onNavigateProfileChange(
                        loginState.user.name,
                        loginState.user.email,
                        loginState.user.avatar
                    )
                }
            )
        }

        LoginUiState.Unauthenticated -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Not logged in")
            }
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    user: User,
    onExit: () -> Unit,
    onNavigateProfileChange: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState)
    ) {
        ProfileHeader(
            user = user
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "@${user.login}",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileStats(user = user)

            Spacer(modifier = Modifier.height(32.dp))

            ProfileActions(
                onNavigateProfileChange = onNavigateProfileChange,
                onExit = onExit
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileHeader(
    user: User
) {
    val headerHeight = 170.dp
    val avatarSize = 120.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
    ) {
        Image(
            painter = painterResource(Res.drawable.profile_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.0f),
                            Color.Black.copy(alpha = 0.6f)
                        )
                    )
                )
        )

        ElevatedCard(
            shape = CircleShape,
            modifier = Modifier
                .size(avatarSize)
                .align(Alignment.BottomCenter)
                .offset(y = avatarSize / 2),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
        ) {
            ProfileAvatar(user.avatar, avatarSize)
        }
    }
}

@Composable
fun ProfileAvatar(avatarUrl: String?, size: Dp) {
    SubcomposeAsyncImage(
        model = avatarUrl,
        contentDescription = "User Avatar",
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(size).clip(CircleShape),
    ) {
        val state by painter.state.collectAsState()
        when (state) {
            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(size * 0.4f))
                }
            }

            is AsyncImagePainter.State.Error, AsyncImagePainter.State.Empty -> {
                Image(
                    painter = painterResource(Res.drawable.defaultProfilePhoto),
                    contentDescription = "Default Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is AsyncImagePainter.State.Success -> {
                SubcomposeAsyncImageContent()
            }
        }
    }
}

@Composable
fun ProfileStats(user: User, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(Res.string.your_statistics),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 12.dp),
            color = MaterialTheme.colorScheme.onSurface
        )

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(
                    value = user.coins.toString(),
                    label = stringResource(Res.string.coins),
                    icon = Res.drawable.coins
                )
                VerticalDivider()
                StatItem(
                    value = user.streak.toString(),
                    label = stringResource(Res.string.streak),
                    icon = Res.drawable.streak
                )
                VerticalDivider()
                StatItem(
                    value = user.xp.toString(),
                    label = stringResource(Res.string.xp),
                    icon = Res.drawable.icon_experience
                )
            }
        }
    }
}

@Composable
fun StatItem(
    value: String,
    label: String,
    icon: DrawableResource,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = label,
                modifier = Modifier.size(28.dp),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun VerticalDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier
            .height(40.dp)
            .width(1.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
    )
}

@Composable
fun ProfileActions(
    onNavigateProfileChange: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilledTonalButton(
            onClick = onNavigateProfileChange,
            modifier = Modifier.defaultMinSize(minHeight = OutlinedTextFieldDefaults.MinHeight),
            shape = RoundedCornerShape(50)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(Res.string.change_user_data))
        }

        OutlinedButton(
            onClick = onExit,
            modifier = Modifier.defaultMinSize(minHeight = OutlinedTextFieldDefaults.MinHeight),
            shape = RoundedCornerShape(50),
            border = ButtonDefaults.outlinedButtonBorder().copy(
                brush = Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.outline))
            ),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(Res.string.exit))
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        val mockUser = User(
            id = "1",
            login = "ailinger",
            email = "preview.user@ailingo.app",
            name = "Ailingo User",
            avatar = null, // Test default avatar state
            // avatar = "https://example.com/some_image.jpg",
            coins = 1234,
            streak = 15,
            xp = 5678,
            registration = "2023-10-26T10:00:00Z",
            lastLoginAt = "2024-03-15T12:30:00Z",
            isEmailVerified = true,
            role = "user",
            lastStreakAt = "2023-10-26T10:00:00Z"
        )
        val loginState = LoginUiState.Success(mockUser, token = "fake_token", refreshToken = "fake_refresh")

        ProfileScreen(
            loginState = loginState,
            onExit = { println("Preview Exit Clicked") },
            onNavigateProfileChange = { _, _, _ -> println("Preview Change Data Clicked") }
        )
    }
}