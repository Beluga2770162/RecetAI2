package com.example.recetai

data class Favorite(
    val recipeId: String = "",
    val recipeTitle: String = "",
    val userId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
