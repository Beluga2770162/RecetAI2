package com.example.recetai.data

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Modelo de datos oficial para el historial de consultas de la IA en Cloud Firestore.
 * * @IgnoreExtraProperties Evita interrupciones o crashes si en el futuro decides guardar
 * metadatos adicionales en el backend (ej. ubicación del escaneo o IDs de recetas sugeridas).
 * @Keep Previene que el optimizador R8/ProGuard mutile u ofusque los nombres de las variables
 * al compilar el APK de lanzamiento, manteniendo el mapeo reflexivo de Firebase intacto.
 */
@Keep
@IgnoreExtraProperties
data class History(
    val userId: String = "",
    val ingredients: List<String> = emptyList(),
    val totalResults: Int = 0,
    val date: Long = System.currentTimeMillis()
)