package com.example.recetai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    
    val auth = remember {
        FirebaseAuth.getInstance()
    }

    val firestore = remember {
        FirebaseFirestore.getInstance()
    }

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center

    ) {

        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Únete a RecetAI",
            color =
                MaterialTheme.colorScheme.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Crea tu cuenta para guardar recetas",
            color =
                MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.7f
                )
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text("Nombre Completo")
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Correo Electrónico")
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Contraseña")
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null
                )
            },
            visualTransformation =
                PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
            },
            label = {
                Text("Confirmar Contraseña")
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null
                )
            },
            visualTransformation =
                PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        if (isLoading) {

            CircularProgressIndicator()

        } else {

            Button(

                onClick = {

                    errorMessage = ""

                    when {

                        name.isBlank() ||
                                email.isBlank() ||
                                password.isBlank() ||
                                confirmPassword.isBlank() -> {

                            errorMessage =
                                "Completa todos los campos"
                        }

                        password != confirmPassword -> {

                            errorMessage =
                                "Las contraseñas no coinciden"
                        }

                        password.length < 6 -> {

                            errorMessage =
                                "La contraseña debe tener al menos 6 caracteres"
                        }

                        else -> {

                            isLoading = true

                            auth.createUserWithEmailAndPassword(
                                email.trim(),
                                password.trim()
                            )

                                .addOnSuccessListener { result ->

                                    val firebaseUser =
                                        result.user

                                    firebaseUser?.updateProfile(

                                        UserProfileChangeRequest.Builder()
                                            .setDisplayName(
                                                name.trim()
                                            )
                                            .build()
                                    )

                                    val userData = User(
                                        uid =
                                            firebaseUser?.uid ?: "",
                                        name =
                                            name.trim(),
                                        email =
                                            email.trim()
                                    )

                                    firestore
                                        .collection("users")
                                        .document(
                                            firebaseUser?.uid ?: ""
                                        )
                                        .set(userData)

                                        .addOnSuccessListener {

                                            isLoading = false

                                            onRegisterSuccess()
                                        }

                                        .addOnFailureListener {

                                            isLoading = false

                                            errorMessage =
                                                "Usuario creado pero no guardado en Firestore"
                                        }
                                }

                                .addOnFailureListener {

                                    isLoading = false

                                    errorMessage =
                                        it.localizedMessage
                                            ?: "Error al registrar"
                                }
                        }
                    }
                },

                modifier = Modifier.fillMaxWidth()

            ) {

                Text(
                    text = "Crear Cuenta"
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        TextButton(
            onClick = onNavigateToLogin
        ) {

            Text(
                text = "¿Ya tienes cuenta? Inicia sesión"
            )
        }
    }

}
