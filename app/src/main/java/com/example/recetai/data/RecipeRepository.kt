package com.example.recetai.data

import com.google.firebase.firestore.FirebaseFirestore
import com.example.recetai.ui.recipes.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RecipeRepository {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Obtiene todas las recetas directamente desde la colección de Firestore.
     */
    suspend fun obtenerTodasLasRecetas(): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val snapshot = db.collection("recipes").get().await()
            snapshot.documents.mapNotNull { it.toObject(Recipe::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Busca una receta específica por su identificador de documento.
     */
    suspend fun obtenerRecetaPorId(id: String): Recipe? = withContext(Dispatchers.IO) {
        try {
            val document = db.collection("recipes").document(id).get().await()
            document.toObject(Recipe::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Filtra colecciones en la nube por categorías.
     */
    suspend fun obtenerRecetasPorCategoria(categoria: String): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val snapshot = db.collection("recipes")
                .whereEqualTo("category", categoria)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Recipe::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Filtra recetas que contengan los ingredientes del inventario desde la base de datos.
     * Limita a un máximo de 10 elementos por restricción estricta de Firestore.
     */
    suspend fun obtenerRecetasPorIngredientes(ingredientesBuscados: List<String>): List<Recipe> = withContext(Dispatchers.IO) {
        if (ingredientesBuscados.isEmpty()) return@withContext emptyList()

        try {
            val queryIngredients = if (ingredientesBuscados.size > 10) ingredientesBuscados.take(10) else ingredientesBuscados
            val ingredientesLimpios = queryIngredients.map { it.lowercase().trim() }

            val snapshot = db.collection("recipes")
                .whereArrayContainsAny("ingredients", ingredientesLimpios)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(Recipe::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}