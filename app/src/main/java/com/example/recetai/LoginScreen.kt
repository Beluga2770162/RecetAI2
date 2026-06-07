package com.example.recetai

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {

    val context = LocalContext.current

    val auth = remember {
        FirebaseAuth.getInstance()
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val webClientIdResId =
        context.resources.getIdentifier(
            "default_web_client_id",
            "string",
            context.packageName
        )

    val webClientId =
        if (webClientIdResId != 0)
            context.getString(webClientIdResId)
        else ""

    val gso = remember(webClientId) {

        GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
    }

    val googleSignInClient =
        remember {
            GoogleSignIn.getClient(
                context,
                gso
            )
        }

    val googleLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            val task =
                GoogleSignIn.getSignedInAccountFromIntent(
                    result.data
                )

            try {

                val account =
                    task.getResult(
                        ApiException::class.java
                    )

                val credential =
                    GoogleAuthProvider.getCredential(
                        account.idToken,
                        null
                    )

                auth.signInWithCredential(
                    credential
                )
                    .addOnSuccessListener {

                        isLoading = false

                        onLoginSuccess()
                    }

                    .addOnFailureListener {

                        isLoading = false

                        Toast.makeText(
                            context,
                            "Error de autenticación",
                            Toast.LENGTH_LONG
                        ).show()
                    }

            } catch (_: Exception) {

                isLoading = false
            }
        }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center

    ) {

        Icon(
            imageVector =
                Icons.Default.Restaurant,
            contentDescription = null,
            modifier = Modifier.size(90.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "RecetAI",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Tu cocina inteligente"
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        OutlinedTextField(

            value = email,

            onValueChange = {
                email = it
            },

            label = {
                Text("Correo")
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
            modifier = Modifier.height(12.dp)
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

            modifier = Modifier.fillMaxWidth(),

            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(

            onClick = {

                if (
                    email.isBlank() ||
                    password.isBlank()
                ) {

                    errorMessage =
                        "Completa todos los campos"

                    return@Button
                }

                isLoading = true

                auth.signInWithEmailAndPassword(
                    email.trim(),
                    password.trim()
                )

                    .addOnSuccessListener {

                        isLoading = false

                        onLoginSuccess()
                    }

                    .addOnFailureListener {

                        isLoading = false

                        errorMessage =
                            it.localizedMessage
                                ?: "Error al iniciar sesión"
                    }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)

        ) {

            Text(
                "Iniciar Sesión"
            )
        }

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
            modifier = Modifier.height(16.dp)
        )

        TextButton(
            onClick =
                onNavigateToForgotPassword
        ) {

            Text(
                "¿Olvidaste tu contraseña?"
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        HorizontalDivider()

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedButton(

            onClick = {

                isLoading = true

                googleLauncher.launch(
                    googleSignInClient.signInIntent
                )
            },

            modifier = Modifier.fillMaxWidth()

        ) {

            Text(
                "Continuar con Google"
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        TextButton(
            onClick =
                onNavigateToRegister
        ) {

            Text(
                "¿No tienes cuenta? Regístrate"
            )
        }

        if (isLoading) {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            CircularProgressIndicator()
        }
    }
}

