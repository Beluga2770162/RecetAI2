package com.example.recetai.camera

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class IngredientAnalyzer(
    // Magia de Kotlin: Si no le pasas el diccionario desde Firebase, usa el "Salvavidas" por defecto.
    // Así tu app funciona HOY sin que tengas que cambiar nada más en otras pantallas.
    private val dictionary: Map<String, String> = defaultHybridDictionary,
    private val onIngredientsDetected: (List<String>) -> Unit
) : ImageAnalysis.Analyzer {

    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
    private var lastAnalyzedTimestamp = 0L

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val currentTimestamp = System.currentTimeMillis()

        if (currentTimestamp - lastAnalyzedTimestamp >= 1000) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                labeler.process(image)
                    .addOnSuccessListener { labels ->

                        // Rayos X encendidos para que puedas monitorear en el Logcat
                        labels.forEach {
                            android.util.Log.d("IA_VISION", "Veo: ${it.text} al ${it.confidence * 100}%")
                        }

                        val detectedItems = labels
                            .filter { it.confidence > 0.45f }
                            .map { it.text.lowercase() }
                            // Comparamos contra el diccionario dinámico
                            .mapNotNull { englishLabel ->
                                dictionary.entries.firstOrNull { englishLabel.contains(it.key) }?.value
                            }
                            .distinct()

                        if (detectedItems.isNotEmpty()) {
                            onIngredientsDetected(detectedItems)
                        }
                    }
                    .addOnFailureListener {
                        android.util.Log.e("IA_VISION", "Error en el escáner", it)
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
            lastAnalyzedTimestamp = currentTimestamp
        } else {
            imageProxy.close()
        }
    }

    // --- DICCIONARIO SALVAVIDAS (LOCAL) ---
    companion object {
        val defaultHybridDictionary = mapOf(
            // 1. SALVAVIDAS (Categorías generales para fotos en pantallas)
            "food" to "ingredientes", "fruit" to "fruta", "vegetable" to "verduras",
            "cuisine" to "comida", "meat" to "carne", "poultry" to "ave", "seafood" to "mariscos",

            // 2. ESPECÍFICOS (Para ingredientes físicos reales)
            "apple" to "manzana", "banana" to "plátano", "lemon" to "limón",
            "orange" to "naranja", "strawberry" to "fresa", "grape" to "uva",
            "watermelon" to "sandía", "pineapple" to "piña", "melon" to "melón",
            "peach" to "durazno", "mango" to "mango", "pear" to "pera",

            "tomato" to "tomate", "onion" to "cebolla", "garlic" to "ajo",
            "carrot" to "zanahoria", "potato" to "papa", "lettuce" to "lechuga",
            "pepper" to "pimiento", "mushroom" to "champiñón", "avocado" to "aguacate",
            "chili" to "chile", "corn" to "maíz", "broccoli" to "brócoli",
            "cucumber" to "pepino", "eggplant" to "berenjena", "zucchini" to "calabacita",
            "spinach" to "espinaca", "celery" to "apio",

            "chicken" to "pollo", "beef" to "carne de res",
            "pork" to "cerdo", "fish" to "pescado", "egg" to "huevo", "fried egg" to "huevo frito",
            "milk" to "leche", "cheese" to "queso", "sausage" to "salchicha",

            "bread" to "pan", "pasta" to "pasta", "rice" to "arroz",
            "bean" to "frijol", "salt" to "sal", "sugar" to "azúcar",
            "oil" to "aceite", "butter" to "mantequilla", "coffee" to "café"
        )
    }
}