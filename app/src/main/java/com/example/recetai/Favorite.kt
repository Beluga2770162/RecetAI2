package com.example.recetai

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Modelo de datos oficial para registrar los favoritos de un usuario en Cloud Firestore.
 * * * @IgnoreExtraProperties: Evita cierres inesperados si en el backend de Firestore
 * se añaden nuevas propiedades que esta versión de la app no maneja.
 * * @Keep: Previene que ProGuard/R8 ofusque o cambie el nombre de las variables,
 * asegurando el correcto mapeo automático.
 */
@Keep
@IgnoreExtraProperties
data class Favorite(
    val recipeId: String = "",
    val recipeTitle: String = "",
    val userId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)