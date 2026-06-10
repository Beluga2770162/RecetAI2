package com.example.recetai.ui.recipes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.recetai.R
import com.example.recetai.ui.home.HomeViewModel
import com.example.recetai.ui.home.RecipeHomeCard

@Composable
fun FavoritesScreen(
    viewModel: HomeViewModel,
    onNavigateToDetail: () -> Unit
) {
    val allRecipes by viewModel.recommendedRecipes.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    // Filtramos las recetas de forma reactiva
    val favoriteRecipes = remember(allRecipes, favoriteIds) {
        allRecipes.filter { it.id in favoriteIds }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.my_favorites),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (favoriteRecipes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.empty_favorites_msg),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(favoriteRecipes, key = { it.id }) { recipe ->
                    RecipeHomeCard(
                        recipe = recipe,
                        isFavorite = true, // Siempre es true porque estamos en la lista de favoritos
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