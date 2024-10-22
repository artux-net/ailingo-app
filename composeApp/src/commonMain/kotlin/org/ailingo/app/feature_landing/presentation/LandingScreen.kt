package org.ailingo.app.feature_landing.presentation

import ailingo.composeapp.generated.resources.LandgingPhone
import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.ailingologowithoutbackground
import ailingo.composeapp.generated.resources.backHeader
import ailingo.composeapp.generated.resources.googlePlay
import ailingo.composeapp.generated.resources.login
import ailingo.composeapp.generated.resources.logo
import ailingo.composeapp.generated.resources.made_for
import ailingo.composeapp.generated.resources.press_kit
import ailingo.composeapp.generated.resources.private_police
import ailingo.composeapp.generated.resources.whats_new
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.BookOpen
import compose.icons.feathericons.Clock
import compose.icons.feathericons.Edit3
import compose.icons.feathericons.Gift
import compose.icons.feathericons.Globe
import compose.icons.feathericons.Headphones
import compose.icons.feathericons.MessageSquare
import compose.icons.feathericons.Mic
import compose.icons.feathericons.Phone
import compose.icons.feathericons.Volume2
import org.ailingo.app.core.helper_window_info.rememberWindowInfo
import org.ailingo.app.theme.ColorForMainTextDictionary
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LandingScreen(
    component: LandingScreenComponent
) {
    val screenInfo = rememberWindowInfo()
    val screenHeight = screenInfo.screenHeight
    val screenWidth = screenInfo.screenWidth
    val infoItems = listOf(
        Info(
            FeatherIcons.MessageSquare,
            "Распознавание Речи",
            "Приложение позволяет распознавать и переводить речь на различные иностранные языки."
        ),
        Info(
            FeatherIcons.BookOpen,
            "Обучение Словарному Запасу",
            "Используйте приложение для изучения новых слов и фраз на иностранных языках с помощью карточек."
        ),
        Info(
            FeatherIcons.Volume2,
            "Аудирование и Понимание",
            "Улучшайте навыки аудирования, слушая записи на иностранных языках и проверяйте свое понимание."
        ),
        Info(
            FeatherIcons.Edit3,
            "Практика Письма",
            "Пишите и получайте обратную связь, чтобы совершенствовать свои навыки."
        ),
        Info(
            FeatherIcons.Mic,
            "Интерактивные Упражнения",
            "Проходите интерактивные упражнения, используя микрофон для проверки произношения."
        ),
        Info(
            FeatherIcons.Headphones,
            "Изучение Акцента",
            "Слушайте различные акценты и обучайтесь правильному произношению с помощью звуковых файлов."
        ),
        Info(
            FeatherIcons.Globe,
            "Путешествия и Языковая Практика",
            "Получайте языковую практику, встречаясь с носителями языка во время путешествий."
        ),
        Info(
            FeatherIcons.Clock,
            "Ежедневные Упражнения",
            "Выполняйте ежедневные языковые упражнения для стабильного прогресса в изучении языка."
        ),
        Info(
            FeatherIcons.Gift,
            "Бонусы и Награды",
            "Зарабатывайте бонусы и награды за достижения в изучении иностранного языка."
        ),
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        ) {
            Box(
                modifier = Modifier.height(screenHeight + 100.dp).width(screenWidth)
            ) {
                Image(
                    painter = painterResource(Res.drawable.backHeader),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(screenHeight).width(screenWidth)
                        .clip(
                            RoundedCornerShape(
                                bottomEndPercent = 5,
                                bottomStartPercent = 5
                            )
                        )
                )
                Column {
                    HeaderLanding(component)
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth().padding(start = 64.dp, end = 64.dp)
                    ) {
                        DisplayPhone()
                        Spacer(modifier = Modifier.width(32.dp))
                        DisplayGooglePlay()
                    }
                }
            }
            Spacer(modifier = Modifier.height(64.dp))
            Row(
                modifier = Modifier.padding(start = 64.dp, end = 64.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                InfoRow(infoItems[0])
                InfoRow(infoItems[1])
                InfoRow(infoItems[2])
            }

            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.padding(start = 64.dp, end = 64.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                InfoRow(infoItems[3])
                InfoRow(infoItems[4])
                InfoRow(infoItems[5])
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.padding(start = 64.dp, end = 64.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                InfoRow(infoItems[6])
                InfoRow(infoItems[7])
                InfoRow(infoItems[8])
            }
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = stringResource(Res.string.made_for),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(64.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = FeatherIcons.Phone,
                            contentDescription = null,
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.height(64.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.whats_new),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Blue
                )
                Text(
                    text = stringResource(Res.string.private_police),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Blue
                )
                Text(
                    text = stringResource(Res.string.press_kit),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Blue
                )
            }
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Composable
fun RowScope.InfoRow(info: Info) {
    Row(modifier = Modifier.weight(1f)) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = info.icon,
                contentDescription = info.description,
                tint = Color.Blue
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                info.title,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(info.description, color = Color.DarkGray)
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HeaderLanding(
    component: LandingScreenComponent
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(120.dp)
            .background(Color.LightGray.copy(alpha = 0.1f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize().padding(start = 64.dp, end = 64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ailingologowithoutbackground),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.height(40.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = {
                   component.onEvent(LandingScreenEvent.OnNavigateToLoginScreen)
                }, colors = ButtonDefaults.textButtonColors(
                    contentColor = ColorForMainTextDictionary
                )
            ) {
                Text(
                    stringResource(Res.string.login),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RowScope.DisplayPhone() {
    Image(
        painterResource(Res.drawable.LandgingPhone),
        contentDescription = null,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RowScope.DisplayGooglePlay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = RoundedCornerShape(25),
                modifier = Modifier.size(100.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
            }
            Column {
                Text(
                    "Google play Connect",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
                Text("Free", color = Color.White)
            }
        }
        Text(
            "Best composeApp for learning languages",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )

        Image(
            painter = painterResource(Res.drawable.googlePlay),
            modifier = Modifier.width(200.dp).height(60.dp),
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
    }
}

data class Info(
    val icon: ImageVector,
    val title: String,
    val description: String
)

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}
