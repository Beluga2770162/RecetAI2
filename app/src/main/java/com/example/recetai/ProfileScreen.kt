package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    isDarkMode: Boolean,
    onThemeChanged: (Boolean) -> Unit,
    currentLanguage: String,
    onLanguageChanged: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToContact: () -> Unit
) {
    // El Surface asegura que el fondo cambie según el tema (Claro/Oscuro)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cabecera del Perfil
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Hola, Carlos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            // SECCIÓN: CONFIGURACIÓN
            Text(
                text = "Configuración",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )

            // Switch de Tema Oscuro
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Tema Oscuro", modifier = Modifier.weight(1f))
                Switch(checked = isDarkMode, onCheckedChange = onThemeChanged)
            }

            // --- SELECTOR DE IDIOMA ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Usamos Info para el idioma porque es un icono básico seguro
                Icon(Icons.Default.Info, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Idioma / Sprache", modifier = Modifier.weight(1f))

                Row {
                    FilterChip(
                        selected = currentLanguage == "es",
                        onClick = { onLanguageChanged("es") },
                        label = { Text("ES") }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    FilterChip(
                        selected = currentLanguage == "de",
                        onClick = { onLanguageChanged("de") },
                        label = { Text("DE") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECCIÓN: NAVEGACIÓN
            Text(
                text = "Soporte y Legal",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón a Términos (Contrato con el Diablo)
            OutlinedButton(
                onClick = onNavigateToTerms,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Icon(Icons.Default.Lock, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Términos y Condiciones")
            }

            // Botón a Contacto (WhatsApp Profesional)
            OutlinedButton(
                onClick = onNavigateToContact,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Icon(Icons.Default.Email, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contacto")
            }
        }
    }
}