package com.example.recetai

data class RecipeSuggestion(
    val title: String,
    val description: String,
    val score: Int,
    val missingIngredients: Int
)

object RecipeCerebro {

    private val recetas = listOf(

        Recipe(
            title = "Omelette de Queso",
            description = "Desayuno rápido y nutritivo.",
            ingredients = listOf(
                "huevo",
                "leche",
                "queso"
            )
        ),

        Recipe(
            title = "Ensalada Caprese",
            description = "Receta fresca italiana.",
            ingredients = listOf(
                "tomate",
                "queso",
                "albahaca"
            )
        ),

        Recipe(
            title = "Sándwich Clásico",
            description = "Perfecto para cualquier hora.",
            ingredients = listOf(
                "pan",
                "jamon",
                "queso"
            )
        ),

        Recipe(
            title = "Guacamole",
            description = "Ideal para botanas.",
            ingredients = listOf(
                "aguacate",
                "cebolla",
                "tomate",
                "limon"
            )
        ),

        Recipe(
            title = "Pasta Alfredo",
            description = "Pasta cremosa italiana.",
            ingredients = listOf(
                "pasta",
                "crema",
                "queso"
            )
        ),

        Recipe(
            title = "Tacos de Pollo",
            description = "Receta mexicana tradicional.",
            ingredients = listOf(
                "pollo",
                "tortilla",
                "cebolla"
            )
        )
    )

    fun buscarRecetas(
        ingredientesUsuario: List<String>
    ): List<RecipeSuggestion> {

        if (ingredientesUsuario.isEmpty()) {
            return emptyList()
        }

        val ingredientesNormalizados =
            ingredientesUsuario.map {
                it.trim().lowercase()
            }

        return recetas.map { receta ->

            val coincidencias = receta.ingredients.count {

                    ingredienteReceta ->

                ingredientesNormalizados.contains(
                    ingredienteReceta.lowercase()
                )
            }

            RecipeSuggestion(
                title = receta.title,
                description = receta.description,
                score = coincidencias,
                missingIngredients =
                    receta.ingredients.size - coincidencias
            )
        }
            .filter {
                it.score > 0
            }
            .sortedByDescending {
                it.score
            }
    }

    fun mejorReceta(
        ingredientesUsuario: List<String>
    ): RecipeSuggestion? {

        return buscarRecetas(
            ingredientesUsuario
        ).firstOrNull()
    }
}
