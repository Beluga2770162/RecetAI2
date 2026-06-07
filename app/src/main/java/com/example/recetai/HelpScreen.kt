package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Ayuda")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {

            Text(
                "¿Cómo funciona RecetAI?"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                """
1. Escanea tus ingredientes.

2. Revisa los ingredientes detectados.

3. Busca recetas compatibles.

4. Guarda tus favoritas.

5. Consulta tu historial.
                """
            )
        }
    }
}