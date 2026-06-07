package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RatingScreen() {

    var rating by remember {
        mutableStateOf(5f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            "Califica RecetAI",
            style =
                MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Slider(
            value = rating,
            onValueChange = {
                rating = it
            },
            valueRange = 1f..5f
        )

        Text(
            "Puntuación: ${rating.toInt()}"
        )
    }
}