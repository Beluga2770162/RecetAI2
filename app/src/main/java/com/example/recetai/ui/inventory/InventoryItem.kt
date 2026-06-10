package com.example.recetai.ui.inventory

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Modelo de datos profesional para la entidad de un ingrediente dentro de la alacena (Firestore).
 * * @IgnoreExtraProperties: Evita que la app sufra cierres inesperados (crashes) si en el backend
 * de Firestore se añaden nuevas propiedades o metadatos que esta versión específica no maneja.
 * * @Keep: Previene que R8/ProGuard modifique, mutile o encoja el nombre de los campos mediante
 * ofuscación al compilar el APK final, asegurando que Firestore lea el mapeo correctamente.
 */
@Keep
@IgnoreExtraProperties
data class InventoryItem(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val quantity: Int = 1,
    val category: String = "General",
    val expirationDate: String = "",
    val detectedByAI: Boolean = false
)