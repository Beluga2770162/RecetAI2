package com.example.recetai

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {


    private val _recipes = MutableStateFlow<List<RecipeHome>>(emptyList())
    val recipes: StateFlow<List<RecipeHome>> = _recipes.asStateFlow()

    private val _searchResults = MutableStateFlow<List<RecipeHome>>(emptyList())
    val searchResults: StateFlow<List<RecipeHome>> = _searchResults.asStateFlow()

    private val db = FirebaseFirestore.getInstance()



    init {
        fetchRecipesFromFirebase()
    }

    private fun fetchRecipesFromFirebase() {
    }

    fun findRecipesByIngredients(scannedIngredients: List<String>) {
        if (scannedIngredients.isEmpty()) {
            _searchResults.value = emptyList()
            return
        }

        val matchedRecipes = _recipes.value.filter { recipe ->
            scannedIngredients.any { ingredient ->
                recipe.description.contains(ingredient.trim(), ignoreCase = true)
            }
        }

        _searchResults.value = matchedRecipes
    }
}