package com.example.recetai

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Recipe(

    // ID de Firebase
    val id: String = "",

    // Información básica
    val title: String = "",
    val description: String = "",
    val category: String = "",

    // Imagen
    val imageUrl: String = "",

    // Tiempo y dificultad
    val time: String = "",
    val difficulty: String = "",

    // Ingredientes
    val ingredients: List<String> = emptyList(),

    // Pasos de preparación
    val steps: List<String> = emptyList(),

    // Información nutricional
    val calories: Int = 0,

    // Favoritos
    val isFavorite: Boolean = false,

    // Popularidad
    val rating: Double = 0.0,
    val views: Int = 0,

    // Autor
    val author: String = "RecetAI"
)