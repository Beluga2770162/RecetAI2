package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
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
    onNavigateToContact: () -> Unit,
    favoriteCount: Int = 0,
    historyCount: Int = 0
) {

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.primaryContainer
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(90.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text =
                        user?.displayName
                            ?: "Usuario Invitado",

                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text =
                        user?.email
                            ?: "Sin correo registrado"
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                AssistChip(
                    onClick = {},
                    label = {
                        Text("Cuenta Activa")
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            Card(
                modifier = Modifier.weight(1f)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Icon(
                        Icons.Default.Star,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = favoriteCount.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )

                    Text("Favoritos")
                }
            }

            Card(
                modifier = Modifier.weight(1f)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Icon(
                        Icons.Default.Info,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = historyCount.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )

                    Text("Recetas")
                }
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Preferencias",
            style =
                MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.Settings,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Text(
                        text = "Modo Oscuro",
                        modifier =
                            Modifier.weight(1f)
                    )

                    Switch(
                        checked = isDarkMode,
                        onCheckedChange =
                            onThemeChanged
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Idioma",
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Row {

                    FilterChip(
                        selected =
                            currentLanguage == "es",
                        onClick = {
                            onLanguageChanged("es")
                        },
                        label = {
                            Text("ES")
                        }
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    FilterChip(
                        selected =
                            currentLanguage == "en",
                        onClick = {
                            onLanguageChanged("en")
                        },
                        label = {
                            Text("EN")
                        }
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    FilterChip(
                        selected =
                            currentLanguage == "pt",
                        onClick = {
                            onLanguageChanged("pt")
                        },
                        label = {
                            Text("PT")
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Información",
            style =
                MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedButton(
            onClick = onNavigateToTerms,
            modifier = Modifier.fillMaxWidth()
        ) {

            Icon(
                Icons.Default.Info,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                "Términos y Condiciones"
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedButton(
            onClick = onNavigateToContact,
            modifier = Modifier.fillMaxWidth()
        ) {

            Icon(
                Icons.Default.ContactSupport,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text("Centro de Ayuda")
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Button(

            onClick = {

                FirebaseAuth
                    .getInstance()
                    .signOut()

                onNavigateToLogin()
            },

            modifier = Modifier.fillMaxWidth(),

            colors = ButtonDefaults.buttonColors(
                containerColor =
                    MaterialTheme.colorScheme.error
            )

        ) {

            Icon(
                Icons.Default.Logout,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text("Cerrar Sesión")
        }

        Text(
            text = "RecetAI v1.0",
            style =
                MaterialTheme.typography.bodySmall
        )
    }

}
