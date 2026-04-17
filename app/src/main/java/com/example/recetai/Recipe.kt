package com.example.recetai

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Recipe(
    val title: String = "",
    val time: String = "",
    val difficulty: String = "",
    val imageUrl: String = "",
    // Crucial para comparar con lo que detecte tu Xiaomi
    val ingredients: List<String> = emptyList()
)