package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Configuración")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Tema",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Modo Oscuro")

                Switch(
                    checked = isDarkMode,
                    onCheckedChange = {
                        onThemeChanged(it)
                    }
                )
            }
        }
    }
}