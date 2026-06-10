package com.example.recetai.data

import com.example.recetai.ui.recipes.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// El modelo de datos se mantiene limpio y profesional
data class RecipeSuggestion(
    val recipe: Recipe,
    val score: Int,
    val missingIngredientsCount: Int,
    val matchPercentage: Int
)

class RecipeCerebro(private val repository: RecipeRepository) {

    /**
     * Helper para obtener las recetas de forma segura dentro de una corrutina.
     */
    private suspend fun obtenerBancoDeRecetas(): List<Recipe> {
        return try {
            // Eliminamos la verificación de null si tu repo siempre devuelve List<Recipe>
            repository.obtenerTodasLasRecetas()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Busca y filtra las recetas basándose en los ingredientes del usuario.
     * Ahora es una 'suspend fun' para poder consumir el repositorio correctamente
     * y ejecuta el cálculo pesado en el hilo correcto (Dispatchers. Default).
     */
    suspend fun buscarRecetas(ingredientesUsuario: List<String>): List<RecipeSuggestion> =
        withContext(Dispatchers.Default) { // Usamos Default para operaciones intensivas de CPU

            if (ingredientesUsuario.isEmpty()) return@withContext emptyList()

            // 1. Normalizamos los ingredientes del usuario en un Set O(1)
            val ingredientesUsuarioSet = ingredientesUsuario
                .map { it.trim().lowercase() }
                .toSet()

            // 2. Traemos las recetas desde el repositorio
            val recetas = obtenerBancoDeRecetas()

            // 3. Procesamos la lógica matemática de coincidencia
            recetas.map { receta ->
                // Usamos un Set para los ingredientes de la receta: búsquedas más rápidas
                val ingredientesRecetaSet = receta.ingredients.map { it.trim().lowercase() }.toSet()

                // Intersección de conjuntos: Cuenta cuántos elementos tienen en común de forma eficiente
                val coincidencias = ingredientesRecetaSet.count { it in ingredientesUsuarioSet }
                val totalIngredientes = ingredientesRecetaSet.size

                val porcentaje = if (totalIngredientes > 0) (coincidencias * 100) / totalIngredientes else 0

                RecipeSuggestion(
                    recipe = receta,
                    score = coincidencias,
                    missingIngredientsCount = totalIngredientes - coincidencias,
                    matchPercentage = porcentaje
                )
            }
                .filter { it.score > 0 }
                // Primero prioriza el mayor porcentaje, si empatan, prioriza la que pida menos ingredientes extra
                .sortedWith(compareByDescending<RecipeSuggestion> { it.matchPercentage }
                    .thenBy { it.missingIngredientsCount })
        }

    /**
     * Obtiene la mejor opción disponible de forma asíncrona.
     */
    suspend fun obtenerMejorReceta(ingredientesUsuario: List<String>): RecipeSuggestion? {
        return buscarRecetas(ingredientesUsuario).firstOrNull()
    }
}