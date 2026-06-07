package com.example.recetai

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var email by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Recuperar Contraseña")
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ) {

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            Text(
                text = "Ingresa tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña."
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            OutlinedTextField(

                value = email,

                onValueChange = {
                    email = it
                },

                label = {
                    Text("Correo electrónico")
                },

                leadingIcon = {

                    Icon(
                        Icons.Default.Email,
                        contentDescription = null
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Button(

                onClick = {

                    if (email.isBlank()) {

                        Toast.makeText(
                            context,
                            "Ingresa un correo",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    isLoading = true

                    FirebaseAuth
                        .getInstance()
                        .sendPasswordResetEmail(email.trim())

                        .addOnSuccessListener {

                            isLoading = false

                            Toast.makeText(
                                context,
                                "Correo enviado correctamente",
                                Toast.LENGTH_LONG
                            ).show()

                            onBack()
                        }

                        .addOnFailureListener {

                            isLoading = false

                            Toast.makeText(
                                context,
                                it.localizedMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)

            ) {

                if (isLoading) {

                    CircularProgressIndicator()

                } else {

                    Text(
                        "Enviar enlace"
                    )
                }
            }
        }
    }
}
