package com.roozbehzarei.superwebview.ui.theme

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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.raj.mygrowth.backgroundDark
import com.raj.mygrowth.backgroundDarkHighContrast
import com.raj.mygrowth.backgroundDarkMediumContrast
import com.raj.mygrowth.backgroundLight
import com.raj.mygrowth.backgroundLightHighContrast
import com.raj.mygrowth.backgroundLightMediumContrast
import com.raj.mygrowth.errorContainerDark
import com.raj.mygrowth.errorContainerDarkHighContrast
import com.raj.mygrowth.errorContainerDarkMediumContrast
import com.raj.mygrowth.errorContainerLight
import com.raj.mygrowth.errorContainerLightHighContrast
import com.raj.mygrowth.errorContainerLightMediumContrast
import com.raj.mygrowth.errorDark
import com.raj.mygrowth.errorDarkHighContrast
import com.raj.mygrowth.errorDarkMediumContrast
import com.raj.mygrowth.errorLight
import com.raj.mygrowth.errorLightHighContrast
import com.raj.mygrowth.errorLightMediumContrast
import com.raj.mygrowth.inverseOnSurfaceDark
import com.raj.mygrowth.inverseOnSurfaceDarkHighContrast
import com.raj.mygrowth.inverseOnSurfaceDarkMediumContrast
import com.raj.mygrowth.inverseOnSurfaceLight
import com.raj.mygrowth.inverseOnSurfaceLightHighContrast
import com.raj.mygrowth.inverseOnSurfaceLightMediumContrast
import com.raj.mygrowth.inversePrimaryDark
import com.raj.mygrowth.inversePrimaryDarkHighContrast
import com.raj.mygrowth.inversePrimaryDarkMediumContrast
import com.raj.mygrowth.inversePrimaryLight
import com.raj.mygrowth.inversePrimaryLightHighContrast
import com.raj.mygrowth.inversePrimaryLightMediumContrast
import com.raj.mygrowth.inverseSurfaceDark
import com.raj.mygrowth.inverseSurfaceDarkHighContrast
import com.raj.mygrowth.inverseSurfaceDarkMediumContrast
import com.raj.mygrowth.inverseSurfaceLight
import com.raj.mygrowth.inverseSurfaceLightHighContrast
import com.raj.mygrowth.inverseSurfaceLightMediumContrast
import com.raj.mygrowth.onBackgroundDark
import com.raj.mygrowth.onBackgroundDarkHighContrast
import com.raj.mygrowth.onBackgroundDarkMediumContrast
import com.raj.mygrowth.onBackgroundLight
import com.raj.mygrowth.onBackgroundLightHighContrast
import com.raj.mygrowth.onBackgroundLightMediumContrast
import com.raj.mygrowth.onErrorContainerDark
import com.raj.mygrowth.onErrorContainerDarkHighContrast
import com.raj.mygrowth.onErrorContainerDarkMediumContrast
import com.raj.mygrowth.onErrorContainerLight
import com.raj.mygrowth.onErrorContainerLightHighContrast
import com.raj.mygrowth.onErrorContainerLightMediumContrast
import com.raj.mygrowth.onErrorDark
import com.raj.mygrowth.onErrorDarkHighContrast
import com.raj.mygrowth.onErrorDarkMediumContrast
import com.raj.mygrowth.onErrorLight
import com.raj.mygrowth.onErrorLightHighContrast
import com.raj.mygrowth.onErrorLightMediumContrast
import com.raj.mygrowth.onPrimaryContainerDark
import com.raj.mygrowth.onPrimaryContainerDarkHighContrast
import com.raj.mygrowth.onPrimaryContainerDarkMediumContrast
import com.raj.mygrowth.onPrimaryContainerLight
import com.raj.mygrowth.onPrimaryContainerLightHighContrast
import com.raj.mygrowth.onPrimaryContainerLightMediumContrast
import com.raj.mygrowth.onPrimaryDark
import com.raj.mygrowth.onPrimaryDarkHighContrast
import com.raj.mygrowth.onPrimaryDarkMediumContrast
import com.raj.mygrowth.onPrimaryLight
import com.raj.mygrowth.onPrimaryLightHighContrast
import com.raj.mygrowth.onPrimaryLightMediumContrast
import com.raj.mygrowth.onSecondaryContainerDark
import com.raj.mygrowth.onSecondaryContainerDarkHighContrast
import com.raj.mygrowth.onSecondaryContainerDarkMediumContrast
import com.raj.mygrowth.onSecondaryContainerLight
import com.raj.mygrowth.onSecondaryContainerLightHighContrast
import com.raj.mygrowth.onSecondaryContainerLightMediumContrast
import com.raj.mygrowth.onSecondaryDark
import com.raj.mygrowth.onSecondaryDarkHighContrast
import com.raj.mygrowth.onSecondaryDarkMediumContrast
import com.raj.mygrowth.onSecondaryLight
import com.raj.mygrowth.onSecondaryLightHighContrast
import com.raj.mygrowth.onSecondaryLightMediumContrast
import com.raj.mygrowth.onSurfaceDark
import com.raj.mygrowth.onSurfaceDarkHighContrast
import com.raj.mygrowth.onSurfaceDarkMediumContrast
import com.raj.mygrowth.onSurfaceLight
import com.raj.mygrowth.onSurfaceLightHighContrast
import com.raj.mygrowth.onSurfaceLightMediumContrast
import com.raj.mygrowth.onSurfaceVariantDark
import com.raj.mygrowth.onSurfaceVariantDarkHighContrast
import com.raj.mygrowth.onSurfaceVariantDarkMediumContrast
import com.raj.mygrowth.onSurfaceVariantLight
import com.raj.mygrowth.onSurfaceVariantLightHighContrast
import com.raj.mygrowth.onSurfaceVariantLightMediumContrast
import com.raj.mygrowth.onTertiaryContainerDark
import com.raj.mygrowth.onTertiaryContainerDarkHighContrast
import com.raj.mygrowth.onTertiaryContainerDarkMediumContrast
import com.raj.mygrowth.onTertiaryContainerLight
import com.raj.mygrowth.onTertiaryContainerLightHighContrast
import com.raj.mygrowth.onTertiaryContainerLightMediumContrast
import com.raj.mygrowth.onTertiaryDark
import com.raj.mygrowth.onTertiaryDarkHighContrast
import com.raj.mygrowth.onTertiaryDarkMediumContrast
import com.raj.mygrowth.onTertiaryLight
import com.raj.mygrowth.onTertiaryLightHighContrast
import com.raj.mygrowth.onTertiaryLightMediumContrast
import com.raj.mygrowth.outlineDark
import com.raj.mygrowth.outlineDarkHighContrast
import com.raj.mygrowth.outlineDarkMediumContrast
import com.raj.mygrowth.outlineLight
import com.raj.mygrowth.outlineLightHighContrast
import com.raj.mygrowth.outlineLightMediumContrast
import com.raj.mygrowth.outlineVariantDark
import com.raj.mygrowth.outlineVariantDarkHighContrast
import com.raj.mygrowth.outlineVariantDarkMediumContrast
import com.raj.mygrowth.outlineVariantLight
import com.raj.mygrowth.outlineVariantLightHighContrast
import com.raj.mygrowth.outlineVariantLightMediumContrast
import com.raj.mygrowth.primaryContainerDark
import com.raj.mygrowth.primaryContainerDarkHighContrast
import com.raj.mygrowth.primaryContainerDarkMediumContrast
import com.raj.mygrowth.primaryContainerLight
import com.raj.mygrowth.primaryContainerLightHighContrast
import com.raj.mygrowth.primaryContainerLightMediumContrast
import com.raj.mygrowth.primaryDark
import com.raj.mygrowth.primaryDarkHighContrast
import com.raj.mygrowth.primaryDarkMediumContrast
import com.raj.mygrowth.primaryLight
import com.raj.mygrowth.primaryLightHighContrast
import com.raj.mygrowth.primaryLightMediumContrast
import com.raj.mygrowth.scrimDark
import com.raj.mygrowth.scrimDarkHighContrast
import com.raj.mygrowth.scrimDarkMediumContrast
import com.raj.mygrowth.scrimLight
import com.raj.mygrowth.scrimLightHighContrast
import com.raj.mygrowth.scrimLightMediumContrast
import com.raj.mygrowth.secondaryContainerDark
import com.raj.mygrowth.secondaryContainerDarkHighContrast
import com.raj.mygrowth.secondaryContainerDarkMediumContrast
import com.raj.mygrowth.secondaryContainerLight
import com.raj.mygrowth.secondaryContainerLightHighContrast
import com.raj.mygrowth.secondaryContainerLightMediumContrast
import com.raj.mygrowth.secondaryDark
import com.raj.mygrowth.secondaryDarkHighContrast
import com.raj.mygrowth.secondaryDarkMediumContrast
import com.raj.mygrowth.secondaryLight
import com.raj.mygrowth.secondaryLightHighContrast
import com.raj.mygrowth.secondaryLightMediumContrast
import com.raj.mygrowth.surfaceBrightDark
import com.raj.mygrowth.surfaceBrightDarkHighContrast
import com.raj.mygrowth.surfaceBrightDarkMediumContrast
import com.raj.mygrowth.surfaceBrightLight
import com.raj.mygrowth.surfaceBrightLightHighContrast
import com.raj.mygrowth.surfaceBrightLightMediumContrast
import com.raj.mygrowth.surfaceContainerDark
import com.raj.mygrowth.surfaceContainerDarkHighContrast
import com.raj.mygrowth.surfaceContainerDarkMediumContrast
import com.raj.mygrowth.surfaceContainerHighDark
import com.raj.mygrowth.surfaceContainerHighDarkHighContrast
import com.raj.mygrowth.surfaceContainerHighDarkMediumContrast
import com.raj.mygrowth.surfaceContainerHighLight
import com.raj.mygrowth.surfaceContainerHighLightHighContrast
import com.raj.mygrowth.surfaceContainerHighLightMediumContrast
import com.raj.mygrowth.surfaceContainerHighestDark
import com.raj.mygrowth.surfaceContainerHighestDarkHighContrast
import com.raj.mygrowth.surfaceContainerHighestDarkMediumContrast
import com.raj.mygrowth.surfaceContainerHighestLight
import com.raj.mygrowth.surfaceContainerHighestLightHighContrast
import com.raj.mygrowth.surfaceContainerHighestLightMediumContrast
import com.raj.mygrowth.surfaceContainerLight
import com.raj.mygrowth.surfaceContainerLightHighContrast
import com.raj.mygrowth.surfaceContainerLightMediumContrast
import com.raj.mygrowth.surfaceContainerLowDark
import com.raj.mygrowth.surfaceContainerLowDarkHighContrast
import com.raj.mygrowth.surfaceContainerLowDarkMediumContrast
import com.raj.mygrowth.surfaceContainerLowLight
import com.raj.mygrowth.surfaceContainerLowLightHighContrast
import com.raj.mygrowth.surfaceContainerLowLightMediumContrast
import com.raj.mygrowth.surfaceContainerLowestDark
import com.raj.mygrowth.surfaceContainerLowestDarkHighContrast
import com.raj.mygrowth.surfaceContainerLowestDarkMediumContrast
import com.raj.mygrowth.surfaceContainerLowestLight
import com.raj.mygrowth.surfaceContainerLowestLightHighContrast
import com.raj.mygrowth.surfaceContainerLowestLightMediumContrast
import com.raj.mygrowth.surfaceDark
import com.raj.mygrowth.surfaceDarkHighContrast
import com.raj.mygrowth.surfaceDarkMediumContrast
import com.raj.mygrowth.surfaceDimDark
import com.raj.mygrowth.surfaceDimDarkHighContrast
import com.raj.mygrowth.surfaceDimDarkMediumContrast
import com.raj.mygrowth.surfaceDimLight
import com.raj.mygrowth.surfaceDimLightHighContrast
import com.raj.mygrowth.surfaceDimLightMediumContrast
import com.raj.mygrowth.surfaceLight
import com.raj.mygrowth.surfaceLightHighContrast
import com.raj.mygrowth.surfaceLightMediumContrast
import com.raj.mygrowth.surfaceVariantDark
import com.raj.mygrowth.surfaceVariantDarkHighContrast
import com.raj.mygrowth.surfaceVariantDarkMediumContrast
import com.raj.mygrowth.surfaceVariantLight
import com.raj.mygrowth.surfaceVariantLightHighContrast
import com.raj.mygrowth.surfaceVariantLightMediumContrast
import com.raj.mygrowth.tertiaryContainerDark
import com.raj.mygrowth.tertiaryContainerDarkHighContrast
import com.raj.mygrowth.tertiaryContainerDarkMediumContrast
import com.raj.mygrowth.tertiaryContainerLight
import com.raj.mygrowth.tertiaryContainerLightHighContrast
import com.raj.mygrowth.tertiaryContainerLightMediumContrast
import com.raj.mygrowth.tertiaryDark
import com.raj.mygrowth.tertiaryDarkHighContrast
import com.raj.mygrowth.tertiaryDarkMediumContrast
import com.raj.mygrowth.tertiaryLight
import com.raj.mygrowth.tertiaryLightHighContrast
import com.raj.mygrowth.tertiaryLightMediumContrast

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

@Immutable
data class ColorFamily(
    val color: Color, val onColor: Color, val colorContainer: Color, val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun SuperWebViewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true, content: @Composable() () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = SuperWebViewTypography, content = content
    )
}

