package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen() {

    var ingredientName by remember {
        mutableStateOf("")
    }

    val ingredientList = remember {
        mutableStateListOf<String>()
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        "Escanear Ingredientes",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }

    ) { innerPadding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)

        ) {

            Text(
                text = "Escáner Inteligente",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Detecta ingredientes y descubre recetas al instante."
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // TARJETA DE ESCÁNER

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = "Escaneo IA",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "Próximamente con CameraX + ML Kit"
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Agregar ingrediente manualmente",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = ingredientName,
                    onValueChange = {
                        ingredientName = it
                    },

                    label = {
                        Text("Ej: Tomate")
                    },

                    modifier = Modifier.weight(1f),

                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                FilledIconButton(

                    onClick = {

                        if (
                            ingredientName.isNotBlank()
                        ) {

                            ingredientList.add(
                                ingredientName.trim()
                            )

                            ingredientName = ""
                        }
                    }

                ) {

                    Icon(
                        Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Ingredientes detectados (${ingredientList.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            if (ingredientList.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = "Aún no hay ingredientes."
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    items(ingredientList) { item ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Row(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),

                                verticalAlignment =
                                    Alignment.CenterVertically,

                                horizontalArrangement =
                                    Arrangement.SpaceBetween

                            ) {

                                Row(
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null
                                    )

                                    Spacer(
                                        modifier = Modifier.width(12.dp)
                                    )

                                    Text(item)
                                }

                                IconButton(

                                    onClick = {
                                        ingredientList.remove(item)
                                    }

                                ) {

                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Eliminar"
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Button(

                onClick = {

                    // Aquí conectarás IngredientReviewScreen
                    // y RecipeCerebro
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                enabled = ingredientList.isNotEmpty()

            ) {

                Icon(
                    Icons.Default.Restaurant,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Buscar Recetas",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}