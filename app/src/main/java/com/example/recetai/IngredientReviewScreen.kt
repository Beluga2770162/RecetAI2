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

    val ingredients = remember {

        mutableStateListOf(
            *initialIngredients.toTypedArray()
        )
    }

    var newIngredient by remember {
        mutableStateOf("")
    }

    Scaffold(

        containerColor =
            MaterialTheme.colorScheme.background,

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Revisar Ingredientes",
                        fontWeight = FontWeight.Bold
                    )
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
                text = "Ingredientes Detectados",
                style =
                    MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text =
                    "Revisa los ingredientes antes de buscar recetas."
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        "Total: ${ingredients.size} ingredientes",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                OutlinedTextField(

                    value = newIngredient,

                    onValueChange = {
                        newIngredient = it
                    },

                    modifier =
                        Modifier.weight(1f),

                    label = {
                        Text("Agregar ingrediente")
                    },

                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                FilledIconButton(

                    onClick = {

                        val ingrediente =
                            newIngredient
                                .trim()
                                .lowercase()

                        if (
                            ingrediente.isNotEmpty() &&
                            !ingredients.contains(
                                ingrediente
                            )
                        ) {

                            ingredients.add(
                                ingrediente
                            )

                            newIngredient = ""
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

            if (ingredients.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            "No hay ingredientes agregados."
                    )
                }

            } else {

                LazyColumn(

                    modifier =
                        Modifier.weight(1f),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)

                ) {

                    items(ingredients) { ingredient ->

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
                                        Icons.Default.Restaurant,
                                        contentDescription = null,
                                        tint =
                                            MaterialTheme.colorScheme.primary
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

                                        ingredients.remove(
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
                modifier = Modifier.height(8.dp)
            )

            OutlinedButton(

                onClick = {

                    ingredients.clear()
                },

                modifier = Modifier.fillMaxWidth()

            ) {

                Text(
                    "Limpiar Lista"
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Button(

                onClick = {

                    onConfirm(
                        ingredients.toList()
                    )
                },

                enabled =
                    ingredients.isNotEmpty(),

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)

            ) {

                Icon(
                    Icons.Default.Restaurant,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text =
                        "Buscar (${ingredients.size}) Ingredientes",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}
