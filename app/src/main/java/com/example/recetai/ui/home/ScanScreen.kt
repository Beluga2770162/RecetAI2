package com.example.recetai.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.example.recetai.R
import com.example.recetai.camera.IngredientAnalyzer

@Composable
fun ScanScreen(
    onSearchRecipes: (List<String>) -> Unit
) {
    var ingredienteManual by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var detectedIngredients by remember { mutableStateOf<List<String>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.scan_ingredients),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- VISOR DE CÁMARA (CameraX) ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            CameraPreviewWithPermissions(
                onIngredientsDetected = { ingredientes ->
                    detectedIngredients = ingredientes
                }
            )
        }

        // Texto dinámico con lo que detecta la IA
        if (detectedIngredients.isNotEmpty()) {
            Text(
                text = "${stringResource(id = R.string.detected_prefix)} ${detectedIngredients.joinToString(", ")}",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- ENTRADA MANUAL ---
        OutlinedTextField(
            value = ingredienteManual,
            onValueChange = { ingredienteManual = it },
            label = { Text(stringResource(id = R.string.manual_input)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isSaving
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isSaving = true
                val listToSend = if (ingredienteManual.isNotBlank()) {
                    listOf(ingredienteManual.trim().lowercase())
                } else {
                    detectedIngredients
                }
                onSearchRecipes(listToSend)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = (ingredienteManual.isNotBlank() || detectedIngredients.isNotEmpty()) && !isSaving
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                // Cambia el texto del botón dependiendo de si escribiste o escaneaste
                Text(
                    if (ingredienteManual.isNotBlank())
                        stringResource(id = R.string.review_manual_ingredient)
                    else
                        stringResource(id = R.string.review_scanned_ingredient)
                )
            }
        }
    }
}

// --- LÓGICA DE LA CÁMARA CON PERMISOS ---
@Composable
fun CameraPreviewWithPermissions(
    onIngredientsDetected: (List<String>) -> Unit
) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> hasPermission = isGranted }

    LaunchedEffect(Unit) {
        if (!hasPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (hasPermission) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(
                                executor,
                                IngredientAnalyzer(onIngredientsDetected = onIngredientsDetected)
                            )
                        }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
                    } catch (e: Exception) {
                        Log.e("CameraPreview", "Fallo al vincular la cámara", e)
                    }
                }, executor)

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(id = R.string.camera_permission_required))
        }
    }
}