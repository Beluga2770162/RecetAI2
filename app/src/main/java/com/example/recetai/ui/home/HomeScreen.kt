package com.example.recetai.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.recetai.R
import com.example.recetai.ui.recipes.FavoriteButton
import com.example.recetai.ui.recipes.Recipe
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToScan: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToDetail: () -> Unit
) {
    // Observamos los estados desde el ViewModel
    val recipes by viewModel.recommendedRecipes.collectAsState()
    val inventoryCount by viewModel.inventoryCount.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val user = FirebaseAuth.getInstance().currentUser

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // --- TARJETA DE BIENVENIDA ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    val defaultName = stringResource(id = R.string.default_chef_name)
                    val displayName = user?.displayName?.ifEmpty { defaultName } ?: defaultName

                    Text(
                        text = "${stringResource(id = R.string.welcome_chef)}, $displayName",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    // Inyectamos la variable dinámica %d en el stringResource
                    Text(
                        text = stringResource(id = R.string.inventory_count_msg, inventoryCount),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.recommended),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- LISTA DE RECETAS ---
            if (recipes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.loading),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(recipes, key = { it.id }) { recipe ->
                        RecipeHomeCard(
                            recipe = recipe,
                            isFavorite = favoriteIds.contains(recipe.id),
                            onFavoriteClick = { viewModel.toggleFavorite(recipe) },
                            onClick = {
                                viewModel.selectRecipe(recipe)
                                onNavigateToDetail()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeHomeCard(
    recipe: Recipe,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipe.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            FavoriteButton(isFavorite = isFavorite, onClick = onFavoriteClick)
        }
    }
}