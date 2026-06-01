package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateHome: () -> Unit,
    onNavigateLogin: () -> Unit
) {

    LaunchedEffect(Unit) {

        delay(2500)

        val user =
            FirebaseAuth
                .getInstance()
                .currentUser

        if (user != null) {
            onNavigateHome()
        } else {
            onNavigateLogin()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(

                modifier = Modifier.align(
                    Alignment.Center
                ),

                horizontalAlignment =
                    Alignment.CenterHorizontally

            ) {

                Icon(
                    imageVector =
                        Icons.Default.Restaurant,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    tint =
                        MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "RecetAI",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Tu cocina inteligente",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(
                    modifier = Modifier.height(32.dp)
                )

                CircularProgressIndicator()
            }

            Text(
                text = "Versión 1.0",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}