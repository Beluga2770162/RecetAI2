package com.example.recetai.ui.recipes

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Modelo de datos oficial y unificado para las recetas de RecetAI.
 * * @IgnoreExtraProperties: Evita crashes si agregas campos en Firestore que esta versión
 * de la app aún no mapea.
 * * @Keep: Evita que ProGuard/R8 ofusque o cambie el nombre de los atributos clave
 * necesarios para la deserialización automática desde Cloud Firestore.
 */
@Keep
@IgnoreExtraProperties
data class Recipe(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val difficulty: String = "",
    val timeMinutes: Int = 0,
    val servings: Int = 1,
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val calories: Int = 0,
    val rating: Double = 0.0,
    val views: Int = 0,
    val author: String = "RecetAI",
    val createdAt: Long = System.currentTimeMillis()
)