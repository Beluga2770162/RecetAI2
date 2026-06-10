package com.example.recetai.ui.recipes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.recetai.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IngredientReviewScreen(
    initialIngredients: List<String>,
    onConfirm: (List<String>) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Inicializamos con un set distinct para evitar duplicados visuales iniciales
    val ingredients = remember { mutableStateListOf(*initialIngredients.distinct().toTypedArray()) }
    var newIngredient by remember { mutableStateOf("") }

    val addIngredientAction = {
        val clean = newIngredient.trim()
        if (clean.isNotEmpty() && !ingredients.any { it.equals(clean, ignoreCase = true) }) {
            ingredients.add(clean.lowercase())
            newIngredient = ""
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(24.dp)) {
        // --- CABECERA ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button_desc)
                )
            }
            Text(
                text = stringResource(id = R.string.review_ingredients_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- ENTRADA DE DATOS ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newIngredient,
                onValueChange = { newIngredient = it },
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(id = R.string.add_manually_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { addIngredientAction() })
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilledIconButton(
                onClick = addIngredientAction,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_button_desc)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- LISTA DINÁMICA ---
        if (ingredients.isEmpty()) {
            Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.empty_list_msg),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = ingredients, key = { it }) { ingredient ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                    ) {
                        Row(
                            Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = ingredient.replaceFirstChar { it.uppercase() },
                                modifier = Modifier.padding(start = 16.dp).weight(1f)
                            )
                            IconButton(onClick = { ingredients.remove(ingredient) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = stringResource(id = R.string.delete_desc),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onConfirm(ingredients.toList()) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = ingredients.isNotEmpty()
        ) {
            // Inyectamos el tamaño de la lista de forma dinámica
            Text(
                text = stringResource(id = R.string.search_recipes_count, ingredients.size),
                fontWeight = FontWeight.Bold
            )
        }
    }
}