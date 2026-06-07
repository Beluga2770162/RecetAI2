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
fun ScanScreen(
    onSearchRecipes: (List<String>) -> Unit = {}
) {

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
                        text = "Escáner Inteligente",
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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),

                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.primaryContainer
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
                            imageVector =
                                Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = "CameraX + ML Kit",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "Próxima integración de reconocimiento automático"
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Agregar ingredientes",
                style =
                    MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                OutlinedTextField(

                    value = ingredientName,

                    onValueChange = {
                        ingredientName = it
                    },

                    modifier =
                        Modifier.weight(1f),

                    label = {
                        Text("Ej. Tomate")
                    },

                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                FilledIconButton(

                    onClick = {

                        if (
                            ingredientName
                                .trim()
                                .isNotEmpty()
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
                modifier = Modifier.height(16.dp)
            )

            Text(
                text =
                    "Ingredientes (${ingredientList.size})",
                style =
                    MaterialTheme.typography.titleMedium,
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
                        text =
                            "Agrega ingredientes para comenzar"
                    )
                }

            } else {

                LazyColumn(

                    modifier =
                        Modifier.weight(1f),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)

                ) {

                    items(ingredientList) { ingredient ->

                        Card(
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Row(

                                modifier =
                                    Modifier
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
                                        modifier =
                                            Modifier.width(12.dp)
                                    )

                                    Text(
                                        text = ingredient
                                    )
                                }

                                IconButton(
                                    onClick = {

                                        ingredientList.remove(
                                            ingredient
                                        )
                                    }
                                ) {

                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription =
                                            "Eliminar"
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

                    onSearchRecipes(
                        ingredientList.toList()
                    )
                },

                enabled =
                    ingredientList.isNotEmpty(),

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(60.dp)

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