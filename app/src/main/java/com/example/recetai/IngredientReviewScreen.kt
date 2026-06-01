package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientReviewScreen(
    initialIngredients: List<String>,
    onConfirm: (List<String>) -> Unit
) {

    val ingredients =
        remember {
            mutableStateListOf(
                *initialIngredients.toTypedArray()
            )
        }

    var newIngredient by remember {
        mutableStateOf("")
    }

    Scaffold(

        containerColor = Color(0xFF0F172A),

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Revisar Ingredientes",
                        color = Color.White
                    )
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0F172A)
                    )
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
                text = "Ingredientes detectados",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "La IA puede equivocarse. Agrega o elimina ingredientes antes de buscar recetas.",
                color = Color.LightGray
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E293B)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Detectados: ${ingredients.size} ingredientes",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextField(
                    value = newIngredient,
                    onValueChange = {
                        newIngredient = it
                    },

                    placeholder = {
                        Text("Agregar ingrediente...")
                    },

                    modifier = Modifier.weight(1f),

                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E293B),
                        unfocusedContainerColor = Color(0xFF1E293B)
                    )
                )

                IconButton(

                    onClick = {

                        if (newIngredient.isNotBlank()) {

                            ingredients.add(
                                newIngredient.trim()
                            )

                            newIngredient = ""
                        }
                    }

                ) {

                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = Color(0xFF10B981)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            if (ingredients.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "No hay ingredientes detectados.",
                        color = Color.Gray
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(ingredients) { ingredient ->

                        Card(

                            modifier = Modifier.fillMaxWidth(),

                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1E293B)
                            )

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
                                        Icons.Default.Restaurant,
                                        contentDescription = null,
                                        tint = Color(0xFF10B981)
                                    )

                                    Spacer(
                                        modifier = Modifier.width(12.dp)
                                    )

                                    Text(
                                        text = ingredient,
                                        color = Color.White
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        ingredients.remove(
                                            ingredient
                                        )
                                    }
                                ) {

                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = Color.Red
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

                    onConfirm(
                        ingredients.toList()
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF10B981)
                )

            ) {

                Text(
                    text = "Buscar Recetas",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}
