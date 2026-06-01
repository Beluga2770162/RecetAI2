package com.example.recetai.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema para MODO OSCURO (Tu diseño de Figma)
private val DarkColorScheme = darkColorScheme(
    primary = Emerald400,
    secondary = Slate800,
    tertiary = Emerald500,
    background = Slate900,
    surface = Slate800,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White, // Esto hace que el texto sea blanco en modo oscuro
    onSurface = Color.White
)

// Esquema para MODO CLARO (Limpio y legible)
private val LightColorScheme = lightColorScheme(
    primary = Emerald500,
    secondary = BlueGray100,
    tertiary = Emerald400,
    background = BlueGray50,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Slate700,
    onBackground = Slate700, // Esto hace que el texto sea gris oscuro/negro en modo claro
    onSurface = Slate700
)

@Composable
fun RecetAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    // Ajuste de la barra de estado de Android (la de arriba donde sale la hora)
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}