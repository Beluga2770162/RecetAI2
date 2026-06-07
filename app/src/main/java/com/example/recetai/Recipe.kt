package com.example.recetai

import com.google.firebase.firestore.IgnoreExtraProperties

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