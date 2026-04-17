package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
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
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.primary)
            Text(text = "Hola, Carlos", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(24.dp))

            // --- SECCIÓN: CUENTA ---
            Text(text = "Cuenta", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Button(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.Default.Login, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Login")
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = onNavigateToRegister,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Registro")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- SECCIÓN: CONFIGURACIÓN ---
            Text(text = "Preferencias", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Tema Oscuro", modifier = Modifier.weight(1f))
                Switch(checked = isDarkMode, onCheckedChange = onThemeChanged)
            }

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Idioma", modifier = Modifier.weight(1f))
                Row {
                    FilterChip(selected = currentLanguage == "es", onClick = { onLanguageChanged("es") }, label = { Text("ES") })
                    Spacer(modifier = Modifier.width(4.dp))
                    FilterChip(selected = currentLanguage == "de", onClick = { onLanguageChanged("de") }, label = { Text("DE") })
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- SECCIÓN: SOPORTE ---
            Text(text = "Legal", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())

            OutlinedButton(onClick = onNavigateToTerms, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Icon(Icons.Default.Lock, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Términos y Condiciones")
            }

            OutlinedButton(onClick = onNavigateToContact, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Icon(Icons.Default.Email, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contacto")
            }
        }
    }
}