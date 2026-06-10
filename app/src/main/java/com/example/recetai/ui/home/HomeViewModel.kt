package com.example.recetai.ui.home

import android.app.Application
import android.content.Context
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // --- NUEVAS DEPENDENCIAS DE ARQUITECTURA ---
    private val repository = RecipeRepository()
    private val cerebro = RecipeCerebro(repository)

    // Estados observables de UI
    private val _recommendedRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recommendedRecipes = _recommendedRecipes.asStateFlow()

    val searchResults = MutableStateFlow<List<Recipe>>(emptyList())
    val favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedRecipe = MutableStateFlow<Recipe?>(null)

    val inventoryCount = MutableStateFlow(0)
    val historyCount = MutableStateFlow(0)
    val favoriteCount = MutableStateFlow(0)
    val lastScannedIngredients = MutableStateFlow<List<String>>(emptyList())

    init {
        loadData()
    }

    private fun loadData() {
        val uid = auth.currentUser?.uid ?: return

        // 1. Escuchar Inventario y actualizar recomendaciones
        db.collection("inventory")
            .whereEqualTo("userId", uid)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val ingredientes = snapshot.documents.mapNotNull { it.getString("name") }
                    inventoryCount.value = snapshot.size()

                    if (ingredientes.isNotEmpty()) {
                        generarRecomendacionesInicio(ingredientes)
                    } else {
                        _recommendedRecipes.value = emptyList() // Limpia si no hay inventario
                    }
                }
            }

        // 2. Escuchar Favoritos
        db.collection("favorites")
            .whereEqualTo("userId", uid)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    favoriteIds.value = snapshot.documents.mapNotNull { it.getString("recipeId") }.toSet()
                    favoriteCount.value = snapshot.size()
                }
            }

        // 3. Escuchar Historial
        db.collection("history")
            .whereEqualTo("userId", uid)
            .addSnapshotListener { snapshot, _ ->
                historyCount.value = snapshot?.size() ?: 0
            }
    }

    // --- LÓGICA DE BÚSQUEDA OPTIMIZADA (USANDO EL CEREBRO) ---

    fun findRecipesOptimized(ingredients: List<String>) {
        if (ingredients.isEmpty()) return

        viewModelScope.launch {
            // 1. El Cerebro consulta el Repositorio (Firestore) y ordena las recetas
            val sugerencias = cerebro.buscarRecetas(ingredients)

            if (sugerencias.isNotEmpty()) {
                // 2. Extraemos el objeto Recipe de la sugerencia ya rankeada
                searchResults.value = sugerencias.map { it.recipe }
            } else {
                // Fallback a IA (Gemini) si la base de datos no arroja resultados
                try {
                    val recetaTexto = GeminiService.generarRecetas(ingredients)
                    searchResults.value = listOf(
                        Recipe(
                            id = "ia_${System.currentTimeMillis()}",
                            title = "Sugerencia de IA",
                            description = recetaTexto,
                            ingredients = ingredients,
                            category = "IA"
                        )
                    )
                } catch (e: Exception) {
                    Log.e("RecetAI_Error", "La IA falló: ${e.message}")
                    searchResults.value = emptyList()
                }
            }
        }
    }

    private fun generarRecomendacionesInicio(ingredients: List<String>) {
        viewModelScope.launch {
            // Reutilizamos el cerebro para que el Home también muestre lo más relevante primero
            val sugerencias = cerebro.buscarRecetas(ingredients)
            _recommendedRecipes.value = sugerencias.map { it.recipe }
        }
    }

    // --- ACCIONES DEL USUARIO ---

    fun toggleFavorite(recipe: Recipe) {
        val uid = auth.currentUser?.uid ?: return

        if (favoriteIds.value.contains(recipe.id)) {
            db.collection("favorites")
                .whereEqualTo("userId", uid)
                .whereEqualTo("recipeId", recipe.id)
                .get()
                .addOnSuccessListener { snapshot ->
                    for (doc in snapshot) doc.reference.delete()
                }
        } else {
            db.collection("favorites").add(
                Favorite(recipeId = recipe.id, recipeTitle = recipe.title, userId = uid)
            )
        }
    }

    fun guardarIngredientesAlInventario(ingredients: List<String>) {
        val uid = auth.currentUser?.uid ?: return
        // Batch write para guardar el inventario de golpe
        val batch = db.batch()
        ingredients.forEach { name ->
            val ref = db.collection("inventory").document()
            batch.set(ref, hashMapOf("name" to name, "userId" to uid, "timestamp" to System.currentTimeMillis()))
        }
        batch.commit()
    }

    fun guardarEnHistorial(ingredients: List<String>) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("history").add(
            hashMapOf("userId" to uid, "ingredients" to ingredients, "date" to System.currentTimeMillis())
        )
    }

    // --- INICIALIZACIÓN DE DATOS LOCALES ---

    fun inicializarBaseDeDatos(jsonRecetas: String) {
        viewModelScope.launch {
            val prefs = getApplication<Application>().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            if (!prefs.getBoolean("ya_se_cargaron", false)) {
                try {
                    val lista: List<Recipe> = Gson().fromJson(jsonRecetas, object : TypeToken<List<Recipe>>() {}.type)
                    val batch = db.batch()

                    lista.forEach { receta ->
                        val ref = db.collection("recipes").document(receta.id)
                        batch.set(ref, receta)
                    }

                    batch.commit().addOnSuccessListener {
                        prefs.edit().putBoolean("ya_se_cargaron", true).apply()
                        Log.d("RecetAI", "Base de datos inicializada correctamente con ${lista.size} recetas.")
                    }.addOnFailureListener { e ->
                        Log.e("RecetAI_Error", "Fallo al inicializar DB: ${e.message}")
                    }
                } catch (e: Exception) {
                    Log.e("RecetAI_Error", "Error al parsear JSON: ${e.message}")
                }
            }
        }
    }

    fun selectRecipe(recipe: Recipe) {
        selectedRecipe.value = recipe
    }

    fun setIngredients(ingredients: List<String>) {
        lastScannedIngredients.value = ingredients
    }
}