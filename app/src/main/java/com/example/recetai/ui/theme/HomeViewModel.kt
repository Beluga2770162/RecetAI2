package com.example.recetai

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _recipes =
        MutableStateFlow<List<RecipeHome>>(emptyList())

    val recipes: StateFlow<List<RecipeHome>> =
        _recipes.asStateFlow()

    private val _searchResults =
        MutableStateFlow<List<RecipeHome>>(emptyList())

    val searchResults: StateFlow<List<RecipeHome>> =
        _searchResults.asStateFlow()

    private val db =
        FirebaseFirestore.getInstance()

    init {
        fetchRecipesFromFirebase()
    }

    private fun fetchRecipesFromFirebase() {

        db.collection("recipes")

            .get()

            .addOnSuccessListener { documents ->

                val recipeList =
                    mutableListOf<RecipeHome>()

                for (document in documents) {

                    val recipe = RecipeHome(

                        id = document.id,

                        title =
                            document.getString("title")
                                ?: "",

                        description =
                            document.getString("description")
                                ?: "",

                        time =
                            document.getString("time")
                                ?: "Sin tiempo"
                    )

                    recipeList.add(recipe)
                }

                _recipes.value = recipeList

                Log.d(
                    "RecetAI",
                    "Recetas cargadas: ${recipeList.size}"
                )
            }

            .addOnFailureListener { e ->

                Log.e(
                    "RecetAI",
                    "Error Firebase",
                    e
                )

                cargarRecetasLocales()
            }
    }

    private fun cargarRecetasLocales() {

        _recipes.value = listOf(

            RecipeHome(
                id = "1",
                title = "Omelette de Queso",
                description = "Huevo, leche y queso.",
                time = "10 min"
            ),

            RecipeHome(
                id = "2",
                title = "Ensalada Caprese",
                description = "Tomate, queso y albahaca.",
                time = "15 min"
            ),

            RecipeHome(
                id = "3",
                title = "Sándwich Clásico",
                description = "Pan, jamón y queso.",
                time = "5 min"
            )
        )
    }

    fun findRecipesByIngredients(
        scannedIngredients: List<String>
    ) {

        if (scannedIngredients.isEmpty()) {

            _searchResults.value =
                emptyList()

            return
        }

        val matchedRecipes =

            _recipes.value.filter { recipe ->

                scannedIngredients.any { ingredient ->

                    recipe.description.contains(
                        ingredient.trim(),
                        ignoreCase = true
                    )
                }
            }

        _searchResults.value =
            matchedRecipes
    }

    fun clearSearch() {

        _searchResults.value =
            emptyList()
    }
}