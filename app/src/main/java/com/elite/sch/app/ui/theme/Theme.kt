package com.elite.sch.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EliteDarkColorScheme = darkColorScheme(
    primary = EliteTurquoise,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = ElitePurple,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = EliteActionOrange,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    background = EliteBackground,
    onBackground = EliteTextPrimary,
    surface = EliteCard,
    onSurface = EliteTextPrimary,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = EliteTextSecondary,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    outline = EliteBorder,
    outlineVariant = OutlineVariantDark,
    scrim = ScrimDark,
    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceDark,
    error = EliteAlert,
    onError = EliteTextPrimary
)

@Composable
fun EliteTheme(
    darkTheme: Boolean = true, // Tema escuro único
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EliteDarkColorScheme,
        typography = Typography,
        shapes = EliteShapes,
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
    EliteTheme(darkTheme = true, content = content)
}
