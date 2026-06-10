package com.example.recetai.ui.auth

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Modelo de datos profesional para la entidad de Usuario en Cloud Firestore.
 * * @IgnoreExtraProperties evita que la app falle si en la base de datos de Firestore
 * existen campos nuevos que esta versión de la app aún no reconoce.
 * * @Keep evita que el optimizador de código (ProGuard/R8) borre o cambie los nombres
 * de las variables al compilar la versión final de lanzamiento (Release).
 */
@Keep
@IgnoreExtraProperties
data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val createdAt: Long = System.currentTimeMillis()
)