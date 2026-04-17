package com.example.recetai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TermsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(24.dp)
    ) {
        Text(
            text = "Términos y Condiciones",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Text(
                text = "Al usar RecetAI, aceptas que procesemos las imágenes de tus ingredientes mediante ML Kit para generar recomendaciones de recetas. Tu privacidad es importante para nosotros y no compartimos tus datos con terceros...",
                color = Color.LightGray
            )
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) {
            Text("Entendido", color = Color.White)
        }
    }
}
