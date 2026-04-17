package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
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
    var showLanguageDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile_title), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // --- SECCIÓN: CUENTA ---
            item { SectionTitle("Cuenta") }
            item {
                Button(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null, tint = Color.Black)
                    Spacer(Modifier.width(8.dp))
                    Text("Continuar con Google", color = Color.Black)
                }
            }
            item {
                TextButton(onClick = onNavigateToRegister, modifier = Modifier.fillMaxWidth()) {
                    Text("Registrarse", color = MaterialTheme.colorScheme.primary)
                }
            }

            // --- SECCIÓN: PREFERENCIAS ---
            item { Spacer(Modifier.height(24.dp)) }
            item { SectionTitle(stringResource(R.string.settings_title)) }

            // Interruptor de Tema
            item {
                PreferenceItem(
                    title = stringResource(R.string.dark_mode),
                    icon = Icons.Default.DarkMode,
                    trailing = {
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = onThemeChanged
                        )
                    }
                )
            }

            // Selector de Idioma
            item {
                PreferenceItem(
                    title = "Idioma / Sprache",
                    icon = Icons.Default.Language,
                    trailing = {
                        Text(
                            text = if (currentLanguage == "de") "Deutsch" else "Español",
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = { showLanguageDialog = true }
                )
            }

            // --- SECCIÓN: MÁS ---
            item { Spacer(Modifier.height(24.dp)) }
            item { SectionTitle("Más") }

            // Botón de Valorar (¡Ya regresó!)
            item {
                PreferenceItem(
                    title = stringResource(R.string.rate_app),
                    icon = Icons.Default.Star,
                    onClick = { /* Lógica para abrir tienda o diálogo */ }
                )
            }

            item {
                PreferenceItem(
                    title = stringResource(R.string.terms),
                    icon = Icons.Default.Description,
                    onClick = onNavigateToTerms
                )
            }

            item {
                PreferenceItem(
                    title = stringResource(R.string.contact),
                    icon = Icons.Default.Email,
                    onClick = onNavigateToContact
                )
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }

    // Diálogo de Selección de Idioma
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Seleccionar Idioma") },
            text = {
                Column {
                    LanguageOption("Español", "es") { code ->
                        onLanguageChanged(code)
                        showLanguageDialog = false
                    }
                    LanguageOption("Deutsch", "de") { code ->
                        onLanguageChanged(code)
                        showLanguageDialog = false
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun LanguageOption(label: String, code: String, onClick: (String) -> Unit) {
    TextButton(
        onClick = { onClick(code) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun PreferenceItem(
    title: String,
    icon: ImageVector,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Surface(
        onClick = { onClick?.invoke() },
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )
            trailing?.invoke() ?: Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}