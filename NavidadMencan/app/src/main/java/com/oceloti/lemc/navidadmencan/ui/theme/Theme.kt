package com.oceloti.lemc.navidadmencan.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
@Immutable
data class ExtendedColorScheme(
    val customColor1: ColorFamily,
    val customColor2: ColorFamily,
    val customColor3: ColorFamily,
    val customColor4: ColorFamily,
    val customColor5: ColorFamily,
    val customColor6: ColorFamily,
    val customColor7: ColorFamily,
    val customColor8: ColorFamily,
    val customColor9: ColorFamily,
    val customColor10: ColorFamily,
    val customColor11: ColorFamily,
    val customColor12: ColorFamily,
    val customColor13: ColorFamily,
    val customColor14: ColorFamily,
    val customColor15: ColorFamily,
    val customColor16: ColorFamily,
)

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

val extendedLight = ExtendedColorScheme(
    customColor1 = ColorFamily(
        customColor1Light,
        onCustomColor1Light,
        customColor1ContainerLight,
        onCustomColor1ContainerLight,
    ),
    customColor2 = ColorFamily(
        customColor2Light,
        onCustomColor2Light,
        customColor2ContainerLight,
        onCustomColor2ContainerLight,
    ),
    customColor3 = ColorFamily(
        customColor3Light,
        onCustomColor3Light,
        customColor3ContainerLight,
        onCustomColor3ContainerLight,
    ),
    customColor4 = ColorFamily(
        customColor4Light,
        onCustomColor4Light,
        customColor4ContainerLight,
        onCustomColor4ContainerLight,
    ),
    customColor5 = ColorFamily(
        customColor5Light,
        onCustomColor5Light,
        customColor5ContainerLight,
        onCustomColor5ContainerLight,
    ),
    customColor6 = ColorFamily(
        customColor6Light,
        onCustomColor6Light,
        customColor6ContainerLight,
        onCustomColor6ContainerLight,
    ),
    customColor7 = ColorFamily(
        customColor7Light,
        onCustomColor7Light,
        customColor7ContainerLight,
        onCustomColor7ContainerLight,
    ),
    customColor8 = ColorFamily(
        customColor8Light,
        onCustomColor8Light,
        customColor8ContainerLight,
        onCustomColor8ContainerLight,
    ),
    customColor9 = ColorFamily(
        customColor9Light,
        onCustomColor9Light,
        customColor9ContainerLight,
        onCustomColor9ContainerLight,
    ),
    customColor10 = ColorFamily(
        customColor10Light,
        onCustomColor10Light,
        customColor10ContainerLight,
        onCustomColor10ContainerLight,
    ),
    customColor11 = ColorFamily(
        customColor11Light,
        onCustomColor11Light,
        customColor11ContainerLight,
        onCustomColor11ContainerLight,
    ),
    customColor12 = ColorFamily(
        customColor12Light,
        onCustomColor12Light,
        customColor12ContainerLight,
        onCustomColor12ContainerLight,
    ),
    customColor13 = ColorFamily(
        customColor13Light,
        onCustomColor13Light,
        customColor13ContainerLight,
        onCustomColor13ContainerLight,
    ),
    customColor14 = ColorFamily(
        customColor14Light,
        onCustomColor14Light,
        customColor14ContainerLight,
        onCustomColor14ContainerLight,
    ),
    customColor15 = ColorFamily(
        customColor15Light,
        onCustomColor15Light,
        customColor15ContainerLight,
        onCustomColor15ContainerLight,
    ),
    customColor16 = ColorFamily(
        customColor16Light,
        onCustomColor16Light,
        customColor16ContainerLight,
        onCustomColor16ContainerLight,
    ),
)

val extendedDark = ExtendedColorScheme(
    customColor1 = ColorFamily(
        customColor1Dark,
        onCustomColor1Dark,
        customColor1ContainerDark,
        onCustomColor1ContainerDark,
    ),
    customColor2 = ColorFamily(
        customColor2Dark,
        onCustomColor2Dark,
        customColor2ContainerDark,
        onCustomColor2ContainerDark,
    ),
    customColor3 = ColorFamily(
        customColor3Dark,
        onCustomColor3Dark,
        customColor3ContainerDark,
        onCustomColor3ContainerDark,
    ),
    customColor4 = ColorFamily(
        customColor4Dark,
        onCustomColor4Dark,
        customColor4ContainerDark,
        onCustomColor4ContainerDark,
    ),
    customColor5 = ColorFamily(
        customColor5Dark,
        onCustomColor5Dark,
        customColor5ContainerDark,
        onCustomColor5ContainerDark,
    ),
    customColor6 = ColorFamily(
        customColor6Dark,
        onCustomColor6Dark,
        customColor6ContainerDark,
        onCustomColor6ContainerDark,
    ),
    customColor7 = ColorFamily(
        customColor7Dark,
        onCustomColor7Dark,
        customColor7ContainerDark,
        onCustomColor7ContainerDark,
    ),
    customColor8 = ColorFamily(
        customColor8Dark,
        onCustomColor8Dark,
        customColor8ContainerDark,
        onCustomColor8ContainerDark,
    ),
    customColor9 = ColorFamily(
        customColor9Dark,
        onCustomColor9Dark,
        customColor9ContainerDark,
        onCustomColor9ContainerDark,
    ),
    customColor10 = ColorFamily(
        customColor10Dark,
        onCustomColor10Dark,
        customColor10ContainerDark,
        onCustomColor10ContainerDark,
    ),
    customColor11 = ColorFamily(
        customColor11Dark,
        onCustomColor11Dark,
        customColor11ContainerDark,
        onCustomColor11ContainerDark,
    ),
    customColor12 = ColorFamily(
        customColor12Dark,
        onCustomColor12Dark,
        customColor12ContainerDark,
        onCustomColor12ContainerDark,
    ),
    customColor13 = ColorFamily(
        customColor13Dark,
        onCustomColor13Dark,
        customColor13ContainerDark,
        onCustomColor13ContainerDark,
    ),
    customColor14 = ColorFamily(
        customColor14Dark,
        onCustomColor14Dark,
        customColor14ContainerDark,
        onCustomColor14ContainerDark,
    ),
    customColor15 = ColorFamily(
        customColor15Dark,
        onCustomColor15Dark,
        customColor15ContainerDark,
        onCustomColor15ContainerDark,
    ),
    customColor16 = ColorFamily(
        customColor16Dark,
        onCustomColor16Dark,
        customColor16ContainerDark,
        onCustomColor16ContainerDark,
    ),
)

val extendedLightMediumContrast = ExtendedColorScheme(
    customColor1 = ColorFamily(
        customColor1LightMediumContrast,
        onCustomColor1LightMediumContrast,
        customColor1ContainerLightMediumContrast,
        onCustomColor1ContainerLightMediumContrast,
    ),
    customColor2 = ColorFamily(
        customColor2LightMediumContrast,
        onCustomColor2LightMediumContrast,
        customColor2ContainerLightMediumContrast,
        onCustomColor2ContainerLightMediumContrast,
    ),
    customColor3 = ColorFamily(
        customColor3LightMediumContrast,
        onCustomColor3LightMediumContrast,
        customColor3ContainerLightMediumContrast,
        onCustomColor3ContainerLightMediumContrast,
    ),
    customColor4 = ColorFamily(
        customColor4LightMediumContrast,
        onCustomColor4LightMediumContrast,
        customColor4ContainerLightMediumContrast,
        onCustomColor4ContainerLightMediumContrast,
    ),
    customColor5 = ColorFamily(
        customColor5LightMediumContrast,
        onCustomColor5LightMediumContrast,
        customColor5ContainerLightMediumContrast,
        onCustomColor5ContainerLightMediumContrast,
    ),
    customColor6 = ColorFamily(
        customColor6LightMediumContrast,
        onCustomColor6LightMediumContrast,
        customColor6ContainerLightMediumContrast,
        onCustomColor6ContainerLightMediumContrast,
    ),
    customColor7 = ColorFamily(
        customColor7LightMediumContrast,
        onCustomColor7LightMediumContrast,
        customColor7ContainerLightMediumContrast,
        onCustomColor7ContainerLightMediumContrast,
    ),
    customColor8 = ColorFamily(
        customColor8LightMediumContrast,
        onCustomColor8LightMediumContrast,
        customColor8ContainerLightMediumContrast,
        onCustomColor8ContainerLightMediumContrast,
    ),
    customColor9 = ColorFamily(
        customColor9LightMediumContrast,
        onCustomColor9LightMediumContrast,
        customColor9ContainerLightMediumContrast,
        onCustomColor9ContainerLightMediumContrast,
    ),
    customColor10 = ColorFamily(
        customColor10LightMediumContrast,
        onCustomColor10LightMediumContrast,
        customColor10ContainerLightMediumContrast,
        onCustomColor10ContainerLightMediumContrast,
    ),
    customColor11 = ColorFamily(
        customColor11LightMediumContrast,
        onCustomColor11LightMediumContrast,
        customColor11ContainerLightMediumContrast,
        onCustomColor11ContainerLightMediumContrast,
    ),
    customColor12 = ColorFamily(
        customColor12LightMediumContrast,
        onCustomColor12LightMediumContrast,
        customColor12ContainerLightMediumContrast,
        onCustomColor12ContainerLightMediumContrast,
    ),
    customColor13 = ColorFamily(
        customColor13LightMediumContrast,
        onCustomColor13LightMediumContrast,
        customColor13ContainerLightMediumContrast,
        onCustomColor13ContainerLightMediumContrast,
    ),
    customColor14 = ColorFamily(
        customColor14LightMediumContrast,
        onCustomColor14LightMediumContrast,
        customColor14ContainerLightMediumContrast,
        onCustomColor14ContainerLightMediumContrast,
    ),
    customColor15 = ColorFamily(
        customColor15LightMediumContrast,
        onCustomColor15LightMediumContrast,
        customColor15ContainerLightMediumContrast,
        onCustomColor15ContainerLightMediumContrast,
    ),
    customColor16 = ColorFamily(
        customColor16LightMediumContrast,
        onCustomColor16LightMediumContrast,
        customColor16ContainerLightMediumContrast,
        onCustomColor16ContainerLightMediumContrast,
    ),
)

val extendedLightHighContrast = ExtendedColorScheme(
    customColor1 = ColorFamily(
        customColor1LightHighContrast,
        onCustomColor1LightHighContrast,
        customColor1ContainerLightHighContrast,
        onCustomColor1ContainerLightHighContrast,
    ),
    customColor2 = ColorFamily(
        customColor2LightHighContrast,
        onCustomColor2LightHighContrast,
        customColor2ContainerLightHighContrast,
        onCustomColor2ContainerLightHighContrast,
    ),
    customColor3 = ColorFamily(
        customColor3LightHighContrast,
        onCustomColor3LightHighContrast,
        customColor3ContainerLightHighContrast,
        onCustomColor3ContainerLightHighContrast,
    ),
    customColor4 = ColorFamily(
        customColor4LightHighContrast,
        onCustomColor4LightHighContrast,
        customColor4ContainerLightHighContrast,
        onCustomColor4ContainerLightHighContrast,
    ),
    customColor5 = ColorFamily(
        customColor5LightHighContrast,
        onCustomColor5LightHighContrast,
        customColor5ContainerLightHighContrast,
        onCustomColor5ContainerLightHighContrast,
    ),
    customColor6 = ColorFamily(
        customColor6LightHighContrast,
        onCustomColor6LightHighContrast,
        customColor6ContainerLightHighContrast,
        onCustomColor6ContainerLightHighContrast,
    ),
    customColor7 = ColorFamily(
        customColor7LightHighContrast,
        onCustomColor7LightHighContrast,
        customColor7ContainerLightHighContrast,
        onCustomColor7ContainerLightHighContrast,
    ),
    customColor8 = ColorFamily(
        customColor8LightHighContrast,
        onCustomColor8LightHighContrast,
        customColor8ContainerLightHighContrast,
        onCustomColor8ContainerLightHighContrast,
    ),
    customColor9 = ColorFamily(
        customColor9LightHighContrast,
        onCustomColor9LightHighContrast,
        customColor9ContainerLightHighContrast,
        onCustomColor9ContainerLightHighContrast,
    ),
    customColor10 = ColorFamily(
        customColor10LightHighContrast,
        onCustomColor10LightHighContrast,
        customColor10ContainerLightHighContrast,
        onCustomColor10ContainerLightHighContrast,
    ),
    customColor11 = ColorFamily(
        customColor11LightHighContrast,
        onCustomColor11LightHighContrast,
        customColor11ContainerLightHighContrast,
        onCustomColor11ContainerLightHighContrast,
    ),
    customColor12 = ColorFamily(
        customColor12LightHighContrast,
        onCustomColor12LightHighContrast,
        customColor12ContainerLightHighContrast,
        onCustomColor12ContainerLightHighContrast,
    ),
    customColor13 = ColorFamily(
        customColor13LightHighContrast,
        onCustomColor13LightHighContrast,
        customColor13ContainerLightHighContrast,
        onCustomColor13ContainerLightHighContrast,
    ),
    customColor14 = ColorFamily(
        customColor14LightHighContrast,
        onCustomColor14LightHighContrast,
        customColor14ContainerLightHighContrast,
        onCustomColor14ContainerLightHighContrast,
    ),
    customColor15 = ColorFamily(
        customColor15LightHighContrast,
        onCustomColor15LightHighContrast,
        customColor15ContainerLightHighContrast,
        onCustomColor15ContainerLightHighContrast,
    ),
    customColor16 = ColorFamily(
        customColor16LightHighContrast,
        onCustomColor16LightHighContrast,
        customColor16ContainerLightHighContrast,
        onCustomColor16ContainerLightHighContrast,
    ),
)

val extendedDarkMediumContrast = ExtendedColorScheme(
    customColor1 = ColorFamily(
        customColor1DarkMediumContrast,
        onCustomColor1DarkMediumContrast,
        customColor1ContainerDarkMediumContrast,
        onCustomColor1ContainerDarkMediumContrast,
    ),
    customColor2 = ColorFamily(
        customColor2DarkMediumContrast,
        onCustomColor2DarkMediumContrast,
        customColor2ContainerDarkMediumContrast,
        onCustomColor2ContainerDarkMediumContrast,
    ),
    customColor3 = ColorFamily(
        customColor3DarkMediumContrast,
        onCustomColor3DarkMediumContrast,
        customColor3ContainerDarkMediumContrast,
        onCustomColor3ContainerDarkMediumContrast,
    ),
    customColor4 = ColorFamily(
        customColor4DarkMediumContrast,
        onCustomColor4DarkMediumContrast,
        customColor4ContainerDarkMediumContrast,
        onCustomColor4ContainerDarkMediumContrast,
    ),
    customColor5 = ColorFamily(
        customColor5DarkMediumContrast,
        onCustomColor5DarkMediumContrast,
        customColor5ContainerDarkMediumContrast,
        onCustomColor5ContainerDarkMediumContrast,
    ),
    customColor6 = ColorFamily(
        customColor6DarkMediumContrast,
        onCustomColor6DarkMediumContrast,
        customColor6ContainerDarkMediumContrast,
        onCustomColor6ContainerDarkMediumContrast,
    ),
    customColor7 = ColorFamily(
        customColor7DarkMediumContrast,
        onCustomColor7DarkMediumContrast,
        customColor7ContainerDarkMediumContrast,
        onCustomColor7ContainerDarkMediumContrast,
    ),
    customColor8 = ColorFamily(
        customColor8DarkMediumContrast,
        onCustomColor8DarkMediumContrast,
        customColor8ContainerDarkMediumContrast,
        onCustomColor8ContainerDarkMediumContrast,
    ),
    customColor9 = ColorFamily(
        customColor9DarkMediumContrast,
        onCustomColor9DarkMediumContrast,
        customColor9ContainerDarkMediumContrast,
        onCustomColor9ContainerDarkMediumContrast,
    ),
    customColor10 = ColorFamily(
        customColor10DarkMediumContrast,
        onCustomColor10DarkMediumContrast,
        customColor10ContainerDarkMediumContrast,
        onCustomColor10ContainerDarkMediumContrast,
    ),
    customColor11 = ColorFamily(
        customColor11DarkMediumContrast,
        onCustomColor11DarkMediumContrast,
        customColor11ContainerDarkMediumContrast,
        onCustomColor11ContainerDarkMediumContrast,
    ),
    customColor12 = ColorFamily(
        customColor12DarkMediumContrast,
        onCustomColor12DarkMediumContrast,
        customColor12ContainerDarkMediumContrast,
        onCustomColor12ContainerDarkMediumContrast,
    ),
    customColor13 = ColorFamily(
        customColor13DarkMediumContrast,
        onCustomColor13DarkMediumContrast,
        customColor13ContainerDarkMediumContrast,
        onCustomColor13ContainerDarkMediumContrast,
    ),
    customColor14 = ColorFamily(
        customColor14DarkMediumContrast,
        onCustomColor14DarkMediumContrast,
        customColor14ContainerDarkMediumContrast,
        onCustomColor14ContainerDarkMediumContrast,
    ),
    customColor15 = ColorFamily(
        customColor15DarkMediumContrast,
        onCustomColor15DarkMediumContrast,
        customColor15ContainerDarkMediumContrast,
        onCustomColor15ContainerDarkMediumContrast,
    ),
    customColor16 = ColorFamily(
        customColor16DarkMediumContrast,
        onCustomColor16DarkMediumContrast,
        customColor16ContainerDarkMediumContrast,
        onCustomColor16ContainerDarkMediumContrast,
    ),
)

val extendedDarkHighContrast = ExtendedColorScheme(
    customColor1 = ColorFamily(
        customColor1DarkHighContrast,
        onCustomColor1DarkHighContrast,
        customColor1ContainerDarkHighContrast,
        onCustomColor1ContainerDarkHighContrast,
    ),
    customColor2 = ColorFamily(
        customColor2DarkHighContrast,
        onCustomColor2DarkHighContrast,
        customColor2ContainerDarkHighContrast,
        onCustomColor2ContainerDarkHighContrast,
    ),
    customColor3 = ColorFamily(
        customColor3DarkHighContrast,
        onCustomColor3DarkHighContrast,
        customColor3ContainerDarkHighContrast,
        onCustomColor3ContainerDarkHighContrast,
    ),
    customColor4 = ColorFamily(
        customColor4DarkHighContrast,
        onCustomColor4DarkHighContrast,
        customColor4ContainerDarkHighContrast,
        onCustomColor4ContainerDarkHighContrast,
    ),
    customColor5 = ColorFamily(
        customColor5DarkHighContrast,
        onCustomColor5DarkHighContrast,
        customColor5ContainerDarkHighContrast,
        onCustomColor5ContainerDarkHighContrast,
    ),
    customColor6 = ColorFamily(
        customColor6DarkHighContrast,
        onCustomColor6DarkHighContrast,
        customColor6ContainerDarkHighContrast,
        onCustomColor6ContainerDarkHighContrast,
    ),
    customColor7 = ColorFamily(
        customColor7DarkHighContrast,
        onCustomColor7DarkHighContrast,
        customColor7ContainerDarkHighContrast,
        onCustomColor7ContainerDarkHighContrast,
    ),
    customColor8 = ColorFamily(
        customColor8DarkHighContrast,
        onCustomColor8DarkHighContrast,
        customColor8ContainerDarkHighContrast,
        onCustomColor8ContainerDarkHighContrast,
    ),
    customColor9 = ColorFamily(
        customColor9DarkHighContrast,
        onCustomColor9DarkHighContrast,
        customColor9ContainerDarkHighContrast,
        onCustomColor9ContainerDarkHighContrast,
    ),
    customColor10 = ColorFamily(
        customColor10DarkHighContrast,
        onCustomColor10DarkHighContrast,
        customColor10ContainerDarkHighContrast,
        onCustomColor10ContainerDarkHighContrast,
    ),
    customColor11 = ColorFamily(
        customColor11DarkHighContrast,
        onCustomColor11DarkHighContrast,
        customColor11ContainerDarkHighContrast,
        onCustomColor11ContainerDarkHighContrast,
    ),
    customColor12 = ColorFamily(
        customColor12DarkHighContrast,
        onCustomColor12DarkHighContrast,
        customColor12ContainerDarkHighContrast,
        onCustomColor12ContainerDarkHighContrast,
    ),
    customColor13 = ColorFamily(
        customColor13DarkHighContrast,
        onCustomColor13DarkHighContrast,
        customColor13ContainerDarkHighContrast,
        onCustomColor13ContainerDarkHighContrast,
    ),
    customColor14 = ColorFamily(
        customColor14DarkHighContrast,
        onCustomColor14DarkHighContrast,
        customColor14ContainerDarkHighContrast,
        onCustomColor14ContainerDarkHighContrast,
    ),
    customColor15 = ColorFamily(
        customColor15DarkHighContrast,
        onCustomColor15DarkHighContrast,
        customColor15ContainerDarkHighContrast,
        onCustomColor15ContainerDarkHighContrast,
    ),
    customColor16 = ColorFamily(
        customColor16DarkHighContrast,
        onCustomColor16DarkHighContrast,
        customColor16ContainerDarkHighContrast,
        onCustomColor16ContainerDarkHighContrast,
    ),
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable() () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}