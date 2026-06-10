package com.example.recetai.ui.recipes

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.recetai.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RatingScreen(onRatingSubmitted: () -> Unit = {}) {
    val context = LocalContext.current
    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }

    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Mapeamos el número de estrellas a su respectivo ID de recurso de texto
    val feedbackResId = remember(rating) {
        when (rating) {
            1 -> R.string.rating_1_star
            2 -> R.string.rating_2_stars
            3 -> R.string.rating_3_stars
            4 -> R.string.rating_4_stars
            else -> R.string.rating_5_stars
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.rate_experience_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Selector de estrellas
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (i in 1..5) {
                    val isSelected = i <= rating
                    val starColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        label = "ColorEstrella"
                    )
                    Icon(
                        imageVector = if (isSelected) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = stringResource(id = R.string.rate_with_stars_desc, i),
                        tint = starColor,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { rating = i }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = feedbackResId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text(stringResource(id = R.string.tell_us_more_label)) },
                modifier = Modifier.fillMaxWidth().height(140.dp),
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    isLoading = true
                    val user = auth.currentUser
                    val review = hashMapOf(
                        "userId" to (user?.uid ?: "Anonymous"),
                        "userEmail" to (user?.email ?: "Not registered"),
                        "stars" to rating,
                        "comment" to comment.trim(),
                        "timestamp" to System.currentTimeMillis()
                    )

                    db.collection("reviews").add(review)
                        .addOnSuccessListener {
                            isLoading = false
                            Toast.makeText(context, context.getString(R.string.toast_thanks_review), Toast.LENGTH_SHORT).show()
                            onRatingSubmitted()
                        }
                        .addOnFailureListener {
                            isLoading = false
                            val errorMsg = context.getString(R.string.toast_error_review, it.localizedMessage)
                            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                        }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(stringResource(id = R.string.submit_rating_button))
                }
            }
        }

        // Overlay de carga
        if (isLoading) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)) {
                Box(contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            }
        }
    }
}