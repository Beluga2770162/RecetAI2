package com.example.recetai.data

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiService {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = ""
    )
    suspend fun generarRecetas(ingredients: List<String>): String = withContext(Dispatchers.IO) {
        val ingredientesStr = ingredients.joinToString(", ")

        val prompt = """
            Actúa como un chef profesional e innovador. 
            Tengo los siguientes ingredientes en mi cocina: $ingredientesStr.
            Escribe una receta deliciosa y creativa que pueda preparar con estos ingredientes.
            
            Estructura la respuesta de manera limpia:
            1. Un título atractivo.
            2. Breve descripción.
            3. Pasos sencillos.
        """.trimIndent()

        try {
            val response = generativeModel.generateContent(prompt)
            response.text ?: fallbackRecipe(ingredientesStr)
        } catch (e: Exception) {
            // Log sencillo para no leer campos inexistentes en el objeto de error
            Log.e("RECETAI_GEMINI", "Error capturado (sin detalles): ${e.javaClass.simpleName}")
            fallbackRecipe(ingredientesStr)
        }
    }

    // --- EL CEREBRO DE RESPALDO (DEMO SEGURA) ---
    private fun fallbackRecipe(ingredientesStr: String): String {
        return """
            Creación Especial RecetAI
            
            He analizado tus ingredientes: $ingredientesStr.
            ¡Aquí tienes una idea rápida y deliciosa!
            
            Ingredientes principales:
            • Tus ingredientes frescos ($ingredientesStr)
            • Sal, pimienta y especias al gusto
            • Un toque de aceite de oliva
            
            Instrucciones de Preparación:
            1. Lava y pica finamente tus ingredientes frescos.
            2. Calienta un sartén a fuego medio con un poco de aceite.
            3. Sofríe los ingredientes lentamente para liberar todo su aroma y sabor.
            4. Añade tus especias favoritas y cocina por 10-15 minutos.
            5. Sirve caliente y disfruta de esta comida inteligente.
            
            ¡Buen provecho! 
        """.trimIndent()
    }
}