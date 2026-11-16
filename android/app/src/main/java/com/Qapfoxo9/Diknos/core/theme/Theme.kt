package com.Qapfoxo9.Diknos.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TrekColorScheme = darkColorScheme(
    primary = AccentStripe,
    onPrimary = BgBase,
    primaryContainer = SurfaceRaised,
    onPrimaryContainer = TextPrimary,
    secondary = AccentTrail,
    onSecondary = BgBase,
    secondaryContainer = SurfaceSunken,
    onSecondaryContainer = TextPrimary,
    tertiary = Gold,
    onTertiary = BgBase,
    background = BgBase,
    onBackground = TextPrimary,
    surface = SurfaceBase,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    error = Danger,
    onError = TextPrimary,
    outline = SurfaceMuted,
    outlineVariant = Hairline,
    scrim = ShadowTint
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TrekColorScheme,
        typography = AppTypography,
        content = content
    )
}
