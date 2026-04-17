package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(onBack: () -> Unit) {
    Scaffold(
        containerColor = Color(0xFF0F172A),
        topBar = {
            TopAppBar(
                title = { Text("Contacto", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            ContactItem("WhatsApp", "+52 123 456 7890", Icons.Default.Chat)
            ContactItem("Correo", "soporte@recetai.com", Icons.Default.Email)
            ContactItem("Facebook", "/RecetAI_Oficial", Icons.Default.Public)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
            ) {
                Text("Volver", color = Color.White)
            }
        }
    }
}

@Composable
fun ContactItem(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier.padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF4ADE80))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, color = Color.Gray, fontSize = 12.sp)
            Text(value, color = Color.White, fontSize = 16.sp)
        }
    }
}

