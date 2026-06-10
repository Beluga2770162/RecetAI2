package com.example.recetai.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Contenedor de Tema Oficial para RecetAI.
 * Envuelve el árbol de componentes de Jetpack Compose para inyectar los esquemas
 * de color dinámicos (Claro/Oscuro) y la configuración tipográfica global.
 */
@Composable
fun RecetAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Escucha por defecto el tema del sistema, pero acepta el override de tu SettingsScreen
    content: @Composable () -> Unit
) {
    // Consumimos directamente las paletas optimizadas y blindadas de tu Color.kt
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    // Ajuste profesional de la barra de estado nativa de Android (Status Bar)
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Hace que la barra de estado se fusione perfectamente con el color de fondo de tu app
            window.statusBarColor = colorScheme.background.toArgb()

            // Si NO es modo oscuro, le dice a Android que pinte los iconos superiores (hora, wifi) en color oscuro
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Enlaza de forma correcta con tu archivo Type.kt
        content = content
    )
}