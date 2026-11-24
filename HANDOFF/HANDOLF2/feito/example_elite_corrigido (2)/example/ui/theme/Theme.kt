package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AisleDarkColorScheme = darkColorScheme(
    primary = AisleTurquoise,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = AislePurple,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = AisleActionOrange,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    background = AisleBackground,
    onBackground = AisleTextPrimary,
    surface = AisleCard,
    onSurface = AisleTextPrimary,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = AisleTextSecondary,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    outline = AisleBorder,
    outlineVariant = OutlineVariantDark,
    scrim = ScrimDark,
    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceDark,
    error = AisleAlert,
    onError = AisleTextPrimary
)

@Composable
fun AisleTheme(
    darkTheme: Boolean = true, // Tema escuro único
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AisleDarkColorScheme,
        typography = Typography,
        shapes = AisleShapes,
        content = content
    )
}

// Alias para compatibilidade com chamadas existentes
@Composable
fun Primeiro5KTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AisleTheme(darkTheme = true, content = content)
}
