package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeResultScreen(

    recipes: List<RecipeHome>,

    onFavoriteClick: (RecipeHome) -> Unit,

    onBackHome: () -> Unit

) {

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        "Recetas Encontradas"
                    )
                }
            )
        }

    ) { padding ->

        if (recipes.isEmpty()) {

            Box(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),

                contentAlignment =
                    Alignment.Center

            ) {

                Column(

                    horizontalAlignment =
                        Alignment.CenterHorizontally

                ) {

                    Icon(
                        Icons.Default.Restaurant,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        "No encontramos recetas"
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Button(
                        onClick = onBackHome
                    ) {

                        Text("Volver")
                    }
                }
            }

        } else {

            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)

            ) {

                items(recipes) { recipe ->

                    Card(
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(16.dp)
                        ) {

                            Text(
                                recipe.title,
                                style =
                                    MaterialTheme.typography.titleLarge,
                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Text(
                                recipe.description
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Text(
                                "Tiempo: ${recipe.time}"
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
                            )

                            Button(

                                onClick = {

                                    onFavoriteClick(
                                        recipe
                                    )
                                }

                            ) {

                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null
                                )

                                Spacer(
                                    modifier =
                                        Modifier.width(8.dp)
                                )

                                Text(
                                    "Guardar Favorito"
                                )
                            }
                        }
                    }
                }

                item {

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Button(

                        onClick =
                            onBackHome,

                        modifier =
                            Modifier.fillMaxWidth()

                    ) {

                        Text(
                            "Volver al Inicio"
                        )
                    }
                }
            }
        }
    }
}