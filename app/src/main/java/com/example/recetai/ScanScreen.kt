package com.example.recetai

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp // SOLUCIÓN AL ERROR ANTERIOR
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

@OptIn(ExperimentalGetImage::class)
@Composable
fun ScanScreen(onIngredientsDetected: (List<String>) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    // 1. CONFIGURACIÓN DEL CEREBRO (ML KIT)
    val objectDetector = remember {
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableClassification() // Para que ponga nombres como "Tomate"
            .build()
        ObjectDetection.getClient(options)
    }

    var detectedText by remember { mutableStateOf("Apunta a un ingrediente") }

    Box(modifier = Modifier.fillMaxSize()) {
        // 2. VISTA DE CÁMARA (CameraX)
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    // 3. ANALIZADOR: Aquí ocurre la magia de la IA
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also { analysis ->
                            analysis.setAnalyzer(executor) { imageProxy ->
                                val mediaImage = imageProxy.image
                                if (mediaImage != null) {
                                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                                    objectDetector.process(image)
                                        .addOnSuccessListener { objects ->
                                            for (obj in objects) {
                                                val label = obj.labels.firstOrNull()?.text ?: "Objeto"
                                                detectedText = "Detectado: $label"
                                                // Si detecta algo útil, lo mandamos de vuelta
                                                if (label != "Objeto") {
                                                    onIngredientsDetected(listOf(label))
                                                }
                                            }
                                        }
                                        .addOnCompleteListener { imageProxy.close() } // Vital para evitar ANR
                                }
                            }
                        }

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageAnalysis
                    )
                }, executor)
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        // 4. INTERFAZ TIPO FIGMA (Overlay)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .background(Color(0x990F172A), RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(detectedText, color = Color.White, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* Acción manual si se desea */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
            ) {
                Text("Confirmar Ingrediente")
            }
        }
    }
}