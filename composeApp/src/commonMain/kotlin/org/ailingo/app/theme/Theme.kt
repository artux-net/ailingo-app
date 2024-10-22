package org.ailingo.app.theme

import ailingo.composeapp.generated.resources.AtypDisplay_Medium
import ailingo.composeapp.generated.resources.NunitoSans_Bold
import ailingo.composeapp.generated.resources.NunitoSans_Light
import ailingo.composeapp.generated.resources.NunitoSans_Medium
import ailingo.composeapp.generated.resources.NunitoSans_Regular
import ailingo.composeapp.generated.resources.NunitoSans_SemiBold
import ailingo.composeapp.generated.resources.OpenSans_Bold
import ailingo.composeapp.generated.resources.OpenSans_Light
import ailingo.composeapp.generated.resources.OpenSans_Medium
import ailingo.composeapp.generated.resources.OpenSans_Regular
import ailingo.composeapp.generated.resources.OpenSans_SemiBold
import ailingo.composeapp.generated.resources.PTSansCaption_Bold
import ailingo.composeapp.generated.resources.PTSansCaption_Regular
import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.Rubik_Bold
import ailingo.composeapp.generated.resources.Rubik_Light
import ailingo.composeapp.generated.resources.Rubik_Medium
import ailingo.composeapp.generated.resources.Rubik_Regular
import ailingo.composeapp.generated.resources.Rubik_SemiBold
import ailingo.composeapp.generated.resources.Ubuntu_Bold
import ailingo.composeapp.generated.resources.Ubuntu_Light
import ailingo.composeapp.generated.resources.Ubuntu_Medium
import ailingo.composeapp.generated.resources.Ubuntu_Regular
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)

private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

private val AppTypography = Typography(
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 40.sp
    )
)


internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun AppTheme(
    content: @Composable() () -> Unit
) {
    val fontFamilyAtypDisplayMedium = FontFamily(Font(Res.font.AtypDisplay_Medium))
    val fontFamilyUbuntuLight = FontFamily(Font(Res.font.Ubuntu_Light))
    val fontFamilyUbuntuRegular = FontFamily(Font(Res.font.Ubuntu_Regular))
    val fontFamilyUbuntuMedium = FontFamily(Font(Res.font.Ubuntu_Medium))
    val fontFamilyUbuntuBold = FontFamily(Font(Res.font.Ubuntu_Bold))
    val fontFamilyNunitoSansBold = FontFamily(Font(Res.font.NunitoSans_Bold))
    val fontFamilyNunitoSansSemiBold = FontFamily(Font(Res.font.NunitoSans_SemiBold))
    val fontFamilyNunitoSansSemiMedium = FontFamily(Font(Res.font.NunitoSans_Medium))
    val fontFamilyNunitoSansRegular = FontFamily(Font(Res.font.NunitoSans_Regular))
    val fontFamilyNunitoSansLight = FontFamily(Font(Res.font.NunitoSans_Light))
    val fontFamilyOpenSansBold = FontFamily(Font(Res.font.OpenSans_Bold))
    val fontFamilyOpenSansSemiBold = FontFamily(Font(Res.font.OpenSans_SemiBold))
    val fontFamilyOpenSansMedium = FontFamily(Font(Res.font.OpenSans_Medium))
    val fontFamilyOpenSansRegular = FontFamily(Font(Res.font.OpenSans_Regular))
    val fontFamilyOpenSansLight = FontFamily(Font(Res.font.OpenSans_Light))
    val fontFamilyPTSansBold = FontFamily(Font(Res.font.PTSansCaption_Bold))
    val fontFamilyPTSansRegular = FontFamily(Font(Res.font.PTSansCaption_Regular))
    val fontFamilyPTSansRubikBold = FontFamily(Font(Res.font.Rubik_Bold))
    val fontFamilyPTSansRubikSemiBold = FontFamily(Font(Res.font.Rubik_SemiBold))
    val fontFamilyPTSansRubikMedium = FontFamily(Font(Res.font.Rubik_Medium))
    val fontFamilyPTSansRubikRegular = FontFamily(Font(Res.font.Rubik_Regular))
    val fontFamilyPTSansRubikLight = FontFamily(Font(Res.font.Rubik_Light))

    val MyTypography = Typography(
        displayLarge = TextStyle(
            fontFamily = fontFamilyAtypDisplayMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 57.sp,
        ),
        displayMedium = TextStyle(
            fontFamily = fontFamilyUbuntuMedium,
            fontSize = 45.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = fontFamilyNunitoSansBold,
            fontSize = 36.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = fontFamilyPTSansRubikBold,
            fontSize = 32.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = fontFamilyPTSansRubikMedium,
            fontSize = 24.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = fontFamilyPTSansRubikLight,
            fontSize = 20.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = fontFamilyOpenSansMedium,
            fontSize = 22.sp
        ),
        titleMedium = TextStyle(
            fontFamily = fontFamilyPTSansRegular,
            fontSize = 16.sp
        ),
        titleSmall = TextStyle(
            fontFamily = fontFamilyPTSansRubikRegular,
            fontSize = 14.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = fontFamilyPTSansRubikLight,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamilyUbuntuRegular,
            fontSize = 14.sp
        ),
        bodySmall = TextStyle(
            fontFamily = fontFamilyNunitoSansRegular,
            fontSize = 12.sp
        ),
        labelLarge = TextStyle(
            fontFamily = fontFamilyPTSansRubikLight,
            fontSize = 14.sp
        ),
        labelMedium = TextStyle(
            fontFamily = fontFamilyNunitoSansSemiMedium,
            fontSize = 12.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = fontFamilyPTSansRubikLight,
            fontSize = 11.sp,
        )
    )

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = MyTypography,

        shapes = AppShapes,
        content = {
            Surface(content = content)
        }
    )

//    val systemIsDark = isSystemInDarkTheme()
//    val isDarkState = remember { mutableStateOf(systemIsDark) }
//    CompositionLocalProvider(
//        LocalThemeIsDark provides isDarkState
//    ) {
//        val isDark by isDarkState
//        SystemAppearance(!isDark)
//        MaterialTheme(
//            colorScheme = if (isDark) DarkColorScheme else LightColorScheme,
//            typography = AppTypography,
//            shapes = AppShapes,
//            content = {
//                Surface(content = content)
//            }
//        )
//    }
}

@Composable
internal expect fun SystemAppearance(isDark: Boolean)
