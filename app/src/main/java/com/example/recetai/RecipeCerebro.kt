package com.example.recetai

data class RecipeSuggestion(
    val title: String,
    val description: String,
    val score: Int
)

fun buscarReceta(
    ingredientes: List<String>
): RecipeSuggestion {

    val recetas = listOf(

        Triple(
            setOf("egg", "milk"),
            "Omelette Cremoso",
            "Desayuno rápido"
        ),

        Triple(
            setOf("tomato", "cheese"),
            "Ensalada Caprese",
            "Receta fresca"
        ),

        Triple(
            setOf("bread", "ham", "cheese"),
            "Sándwich Clásico",
            "Ideal para cualquier hora"
        )
    )

    var mejorReceta: RecipeSuggestion? = null

    var mejorPuntaje = 0

    for ((ingredientesReceta, titulo, descripcion) in recetas) {

        val coincidencias =
            ingredientes.count {

                ingredientesReceta.contains(
                    it.lowercase()
                )
            }

        if (coincidencias > mejorPuntaje) {

            mejorPuntaje = coincidencias

            mejorReceta =
                RecipeSuggestion(
                    title = titulo,
                    description = descripcion,
                    score = coincidencias
                )
        }
    }

    return mejorReceta
        ?: RecipeSuggestion(
            "Sin coincidencias",
            "Agrega más ingredientes",
            0
        )
}
