package com.example.recetai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientReviewScreen(initialIngredients: List<String>, onConfirm: (List<String>) -> Unit) {
    // Favor de tampoco tocar es lo mas importante de esta pantalla
    val ingredients = remember { mutableStateListOf(*initialIngredients.toTypedArray()) }
    var newIngredient by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFF0F172A),
        topBar = { TopAppBar(title = { Text("Revisar Ingredientes", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            // Campo para agregar manualmente (por si la IA falló)
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = newIngredient,
                    onValueChange = { newIngredient = it },
                    placeholder = { Text("Agregar otro...") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color(0xFF1E293B))
                )
                IconButton(onClick = {
                    if (newIngredient.isNotBlank()) {
                        ingredients.add(newIngredient)
                        newIngredient = ""
                    }
                }) { Icon(Icons.Default.Add, contentDescription = null, tint = Color.Green) }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de lo detectado por el Xiaomi
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(ingredients) { ingredient ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(ingredient, color = Color.White)
                            IconButton(onClick = { ingredients.remove(ingredient) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { onConfirm(ingredients.toList()) },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
            ) { Text("Buscar Recetas") }
        }
    }
}

//Esto dejo de conectarse con la camara al momento de hacer una actualizacion
//Nota de Emilio: Me quiero volver chango
