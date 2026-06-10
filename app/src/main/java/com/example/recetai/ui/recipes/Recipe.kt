package com.example.recetai.ui.recipes

// Es CRUCIAL que tenga valores por defecto para que toObject funcione
data class Recipe(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val author: String = "RecetAI",
    val category: String = "",
    val calories: Long = 0,
    val difficulty: String = "",
    val imageUrl: String = "",
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val timeMinutes: Long = 0,
    val servings: Long = 1,
    val rating: Double = 0.0,
    val views: Long = 0,
    val createdAt: Long = 0
)