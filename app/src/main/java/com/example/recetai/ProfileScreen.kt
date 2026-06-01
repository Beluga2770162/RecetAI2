package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

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

    val user = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // PERFIL
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(90.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user?.displayName ?: "Invitado",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = user?.email ?: "Sin correo registrado"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ESTADÍSTICAS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Card(
                modifier = Modifier.weight(1f)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        Icons.Default.Star,
                        contentDescription = null
                    )

                    Text(
                        "0",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Text("Favoritos")
                }
            }

            Card(
                modifier = Modifier.weight(1f)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        Icons.Default.Info,
                        contentDescription = null
                    )

                    Text(
                        "0",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Text("Recetas")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // CUENTA
        Text(
            text = "Cuenta",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier.weight(1f)
            ) {

                Icon(
                    Icons.Default.Lock,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Login")
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = onNavigateToRegister,
                modifier = Modifier.weight(1f)
            ) {

                Icon(
                    Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Registro")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // PREFERENCIAS
        Text(
            text = "Preferencias",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.Settings,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        "Tema Oscuro",
                        modifier = Modifier.weight(1f)
                    )

                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = onThemeChanged
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.Info,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        "Idioma",
                        modifier = Modifier.weight(1f)
                    )

                    Row {

                        FilterChip(
                            selected = currentLanguage == "es",
                            onClick = { onLanguageChanged("es") },
                            label = { Text("ES") }
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        FilterChip(
                            selected = currentLanguage == "en",
                            onClick = { onLanguageChanged("en") },
                            label = { Text("EN") }
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        FilterChip(
                            selected = currentLanguage == "pt",
                            onClick = { onLanguageChanged("pt") },
                            label = { Text("PT") }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SOPORTE
        Text(
            text = "Soporte",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onNavigateToTerms,
            modifier = Modifier.fillMaxWidth()
        ) {

            Icon(
                Icons.Default.Lock,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("Términos y Condiciones")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onNavigateToContact,
            modifier = Modifier.fillMaxWidth()
        ) {

            Icon(
                Icons.Default.Email,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("Contacto")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "RecetAI v1.0",
            style = MaterialTheme.typography.bodySmall
        )
    }
}