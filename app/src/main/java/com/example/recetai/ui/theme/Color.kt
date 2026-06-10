package com.example.recetai.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// --- PALETA BASE DE COLORES (TAILWIND / MATERIAL SPEC) ---
val Slate900 = Color(0xFF0F172A)
val Slate800 = Color(0xFF1E293B)
val Slate700 = Color(0xFF334155)
val Slate400 = Color(0xFF94A3B8)

val BlueGray50 = Color(0xFFF8FAFC)
val BlueGray100 = Color(0xFFF1F5F9)

val Emerald400 = Color(0xFF4ADE80)
val Emerald500 = Color(0xFF22C55E)
val Emerald600 = Color(0xFF16A34A) // Añadido para mejores contrastes de lectura en modo claro

// --- COLORES SEMÁNTICOS DE NOTIFICACIÓN ---
val ErrorRed = Color(0xFFEF4444)
val SuccessGreen = Color(0xFF10B981)
val WarningYellow = Color(0xFFF59E0B)

// --- MAPEO OFICIAL: TEMA CLARO (LIGHT COLOR SCHEME) ---
val LightColorScheme = lightColorScheme(
    primary = Emerald500,                  // Botones principales, elementos clave de marca
    onPrimary = Color.White,               // Texto o iconos sobre elementos primarios
    primaryContainer = Emerald400.copy(alpha = 0.2f), // Fondos sutiles destacados (ej. insignias)
    onPrimaryContainer = Emerald600,

    secondary = Slate700,                  // Elementos complementarios, chips de categorías
    onSecondary = Color.White,

    background = BlueGray50,               // El fondo general de las pantallas
    onBackground = Slate900,               // Texto principal sobre el fondo de la app

    surface = Color.White,                 // Fondo de tarjetas (Cards), Diálogos y Menús
    onSurface = Slate900,                  // Texto principal dentro de tarjetas
    surfaceVariant = BlueGray100,          // Fondos alternativos de celdas o separadores
    onSurfaceVariant = Slate700,           // Subtextos y etiquetas descriptivas secundarias

    error = ErrorRed,
    onError = Color.White
)

// --- MAPEO OFICIAL: TEMA OSCURO (DARK COLOR SCHEME) ---
val DarkColorScheme = darkColorScheme(
    primary = Emerald400,                  // En modo oscuro, un verde ligeramente más brillante para legibilidad
    onPrimary = Slate900,
    primaryContainer = Emerald500.copy(alpha = 0.3f),
    onPrimaryContainer = Emerald400,

    secondary = Slate400,
    onSecondary = Slate900,

    background = Slate900,                 // Fondo oscuro profundo de alta gama
    onBackground = BlueGray50,             // Texto claro para contraste seguro

    surface = Slate800,                    // Tarjetas elevadas que contrastan con el fondo Slate900
    onSurface = BlueGray50,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate400,

    error = ErrorRed,
    onError = Color.White
)