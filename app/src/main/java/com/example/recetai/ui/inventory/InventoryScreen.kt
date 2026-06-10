package com.example.recetai.ui.inventory

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recetai.R

@Composable
fun InventoryScreen(
    modifier: Modifier = Modifier,
    viewModel: InventoryViewModel = viewModel()
) {
    val inventory by viewModel.inventory.collectAsState()
    var ingredientName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // --- DIÁLOGO DE AGREGAR ---
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; ingredientName = "" },
            title = { Text(stringResource(id = R.string.add_ingredient_title)) },
            text = {
                OutlinedTextField(
                    value = ingredientName,
                    onValueChange = { ingredientName = it },
                    label = { Text(stringResource(id = R.string.ingredient_name_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (ingredientName.isNotBlank()) {
                        viewModel.addIngredient(ingredientName.trim())
                        ingredientName = ""
                        showDialog = false
                    }
                }) { Text(stringResource(id = R.string.save_button)) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; ingredientName = "" }) {
                    Text(stringResource(id = R.string.cancel_button))
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_ingredient_desc)
                )
            }
        }
    ) { padding ->
        Column(modifier = modifier.fillMaxSize().padding(padding).padding(16.dp)) {

            // --- TARJETA RESUMEN ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = inventory.size.toString(), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                    Text(text = stringResource(id = R.string.ingredients_in_pantry), style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(stringResource(id = R.string.my_ingredients), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            // --- LISTA DE INVENTARIO ---
            if (inventory.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(id = R.string.empty_pantry_msg),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(inventory, key = { it.id }) { item ->
                        IngredientRowCard(item = item, onDeleteClick = { viewModel.deleteIngredient(item.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientRowCard(item: InventoryItem, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Restaurant, tint = MaterialTheme.colorScheme.primary, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.padding(end = 8.dp)) {
                    Text(
                        text = item.name.replaceFirstChar { it.uppercase() },
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (item.detectedByAI) {
                        Text(
                            stringResource(id = R.string.detected_by_ai),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = stringResource(id = R.string.delete_desc)
                )
            }
        }
    }
}