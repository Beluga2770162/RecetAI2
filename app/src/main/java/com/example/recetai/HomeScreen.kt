package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth

data class RecipeHome(
    val id: String,
    val title: String,
    val description: String,
    val time: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToScan: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {

    val recipes by viewModel.recipes.collectAsState()

    val favoriteCount by
    viewModel.favoriteCount.collectAsState()

    val historyCount by
    viewModel.historyCount.collectAsState()

    val user =
        FirebaseAuth.getInstance().currentUser

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "RecetAI",
                        fontWeight = FontWeight.Bold
                    )
                },

                actions = {

                    IconButton(
                        onClick = onNavigateToProfile
                    ) {

                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Perfil"
                        )
                    }
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
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "👋 Hola ${user?.displayName ?: "Chef"}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Descubre nuevas recetas con los ingredientes que tienes en casa."
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                StatCard(
                    value = recipes.size.toString(),
                    label = "Recetas",
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    value = favoriteCount.toString(),
                    label = "Favoritos",
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    value = historyCount.toString(),
                    label = "Historial",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(

                onClick = onNavigateToScan,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)

            ) {

                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    "Escanear Ingredientes"
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Recetas Disponibles",
                style =
                    MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            if (recipes.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            "No hay recetas disponibles."
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    items(recipes) { recipe ->

                        RecipeHomeCard(

                            recipe = recipe,

                            onFavoriteClick = {

                                viewModel.addFavorite(
                                    recipe
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Text(label)
        }
    }
}

@Composable
fun RecipeHomeCard(
    recipe: RecipeHome,
    onFavoriteClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = recipe.title,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                IconButton(
                    onClick =
                        onFavoriteClick
                ) {

                    Icon(
                        Icons.Default.Star,
                        contentDescription =
                            "Favorito"
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = recipe.description
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                Text(recipe.time)
            }
        }
    }
}


