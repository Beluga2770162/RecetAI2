package com.example.recetai.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetai.Favorite
import com.example.recetai.data.GeminiService
import com.example.recetai.data.RecipeCerebro
import com.example.recetai.data.RecipeRepository
import com.example.recetai.ui.recipes.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val repository = RecipeRepository()
    private val cerebro = RecipeCerebro(repository)

    // Estados expuestos para la UI
    private val _recommendedRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recommendedRecipes = _recommendedRecipes.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Recipe>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    val favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedRecipe = MutableStateFlow<Recipe?>(null)
    val lastScannedIngredients = MutableStateFlow<List<String>>(emptyList())

    val inventoryCount = MutableStateFlow(0)
    val favoriteCount = MutableStateFlow(0)
    val historyCount = MutableStateFlow(0)

    init {
        loadData()
    }

    private fun loadData() {
        val uid = auth.currentUser?.uid ?: return

        // 1. Escuchar Inventario y filtrar Feed
        db.collection("inventory").whereEqualTo("userId", uid).addSnapshotListener { invSnapshot, _ ->
            val ingredientesAlacena = invSnapshot?.documents?.mapNotNull { it.getString("name")?.lowercase() } ?: emptyList()
            inventoryCount.value = invSnapshot?.size() ?: 0

            db.collection("recipes").get().addOnSuccessListener { recipeSnapshot ->
                val todasLasRecetas = recipeSnapshot.documents.mapNotNull { it.toObject(Recipe::class.java)?.copy(id = it.id) }
                _recommendedRecipes.value = if (ingredientesAlacena.isEmpty()) emptyList()
                else todasLasRecetas.filter { r -> r.ingredients.any { ing -> ingredientesAlacena.contains(ing.lowercase()) } }
            }
        }

        // 2. Escuchar Favoritos
        db.collection("favorites").whereEqualTo("userId", uid).addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                favoriteIds.value = snapshot.documents.mapNotNull { it.getString("recipeId") }.toSet()
                favoriteCount.value = snapshot.size()
            }
        }

        // 3. Escuchar Historial
        db.collection("history").whereEqualTo("userId", uid).addSnapshotListener { snapshot, _ ->
            historyCount.value = snapshot?.size() ?: 0
        }
    }

    // --- FUNCIONES DE ACCIÓN ---

    fun findRecipesOptimized(ingredients: List<String>) {
        if (ingredients.isEmpty()) return

        viewModelScope.launch {
            try {
                // 1. Limpieza de datos
                val cleanIngredients = ingredients.map { it.trim().lowercase() }

                // 2. Búsqueda local
                val sugerencias = cerebro.buscarRecetas(cleanIngredients)

                if (sugerencias.isNotEmpty()) {
                    _searchResults.value = sugerencias.map { it.recipe }
                } else {
                    // 3. Fallback a IA con control total de errores
                    try {
                        val aiResponse = GeminiService.generarRecetas(cleanIngredients)

                        // Si aiResponse llega vacía o nula, evitamos crear la receta
                        if (!aiResponse.isNullOrBlank()) {
                            _searchResults.value = listOf(
                                Recipe(
                                    id = "ia_${System.currentTimeMillis()}",
                                    title = "Sugerencia del Chef IA",
                                    description = aiResponse,
                                    ingredients = cleanIngredients,
                                    category = "IA"
                                )
                            )
                        } else {
                            _searchResults.value = emptyList()
                        }
                    } catch (e: Exception) {
                        Log.e("HomeViewModel", "Error en IA: ${e.message}")
                        _searchResults.value = emptyList() // Fallo silencioso
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error crítico en búsqueda: ${e.message}")
                _searchResults.value = emptyList()
            }
        }
    }

    fun selectRecipe(recipe: Recipe) { selectedRecipe.value = recipe }
    fun setIngredients(ingredients: List<String>) { lastScannedIngredients.value = ingredients }

    fun toggleFavorite(recipe: Recipe) {
        val uid = auth.currentUser?.uid ?: return
        if (favoriteIds.value.contains(recipe.id)) {
            db.collection("favorites").whereEqualTo("userId", uid).whereEqualTo("recipeId", recipe.id).get()
                .addOnSuccessListener { snapshot -> snapshot.forEach { it.reference.delete() } }
        } else {
            db.collection("favorites").add(Favorite(recipeId = recipe.id, recipeTitle = recipe.title, userId = uid))
        }
    }

    fun guardarIngredientesAlInventario(list: List<String>) {
        val uid = auth.currentUser?.uid ?: return
        list.forEach { name ->
            db.collection("inventory").add(hashMapOf("name" to name, "userId" to uid, "timestamp" to System.currentTimeMillis()))
        }
    }

    fun guardarEnHistorial(list: List<String>) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("history").add(hashMapOf("userId" to uid, "ingredients" to list, "date" to System.currentTimeMillis()))
    }
}