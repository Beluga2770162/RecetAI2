package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContratoConElDiablo(
    onBack: () -> Unit
) {

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Términos y Condiciones")
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        }

    ) { innerPadding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(
                    rememberScrollState()
                )

        ) {

            Text(
                text = "Términos de Uso de RecetAI",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            TerminoItem(
                titulo = "1. Uso de la aplicación",
                descripcion = "RecetAI es una aplicación diseñada para ayudar a los usuarios a identificar ingredientes y obtener sugerencias de recetas. El uso de la aplicación debe realizarse de manera responsable y conforme a la legislación aplicable."
            )

            TerminoItem(
                titulo = "2. Privacidad y datos",
                descripcion = "La información proporcionada por el usuario se utiliza únicamente para ofrecer los servicios de la aplicación. RecetAI no comparte información personal con terceros sin autorización del usuario."
            )

            TerminoItem(
                titulo = "3. Permisos del dispositivo",
                descripcion = "La aplicación puede solicitar acceso a la cámara para la detección de ingredientes. Estos permisos son utilizados exclusivamente para las funciones principales de la aplicación."
            )

            TerminoItem(
                titulo = "4. Disponibilidad del servicio",
                descripcion = "Se realizan esfuerzos para mantener la aplicación disponible y actualizada. Sin embargo, algunas funciones pueden verse afectadas por problemas técnicos o de conectividad."
            )

            TerminoItem(
                titulo = "5. Actualizaciones",
                descripcion = "RecetAI puede incorporar mejoras, nuevas funciones y correcciones mediante actualizaciones periódicas."
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Versión 1.0.0",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Última actualización: Mayo 2026",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )
        }
    }
}

@Composable
fun TerminoItem(
    titulo: String,
    descripcion: String
) {

    Column(
        modifier = Modifier.padding(bottom = 20.dp)
    ) {

        Text(
            text = titulo,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = descripcion,
            lineHeight = 22.sp
        )
    }
}
