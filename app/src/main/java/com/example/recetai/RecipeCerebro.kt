package com.example.recetai

import kotlin.collections.iterator

fun buscarReceta(ingredientes: List<String>): String {
    val menu = mapOf(
        setOf("Egg", "Milk") to "¡Puedes hacer un Omelette cremoso!",
        setOf("Tomato", "Cheese") to "¡Sale una ensalada Caprese!",
        setOf("Bread", "Ham", "Cheese") to "Un sándwich clásico nunca falla."
    )

    // Buscamos si lo que tienes coincide con algo de nuestro menú
    for ((necesarios, nombreReceta) in menu) {
        if (ingredientes.containsAll(necesarios.toList())) {
            return nombreReceta
        }
    }
    return "Mmm, intenta agregar más ingredientes para darte una idea."
}
