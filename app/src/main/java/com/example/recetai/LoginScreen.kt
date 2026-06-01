package com.example.recetai

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
    onNavigateToRegister: () -> Unit
) {

    val context = LocalContext.current

    val auth = remember {
        FirebaseAuth.getInstance()
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

    val googleSignInLauncher =
        rememberLauncherForActivityResult(
            contract =
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

                account?.idToken?.let { idToken ->

                    val credential =
                        GoogleAuthProvider.getCredential(
                            idToken,
                            null
                        )

                    auth.signInWithCredential(
                        credential
                    )
                        .addOnSuccessListener {

                            isLoading = false

                            onLoginSuccess()
                        }

                        .addOnFailureListener { e ->

                            isLoading = false

                            Toast.makeText(
                                context,
                                "Error: ${e.localizedMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }

            } catch (e: ApiException) {

                isLoading = false

                Toast.makeText(
                    context,
                    "Error Google: ${e.statusCode}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally

    ) {

        Icon(
            imageVector = Icons.Default.Restaurant,
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
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
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

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "📷 Detecta ingredientes automáticamente"
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "🍅 Reconoce alimentos usando la cámara"
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "🤖 Obtén recetas sugeridas por IA"
                )
            }
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        if (isLoading) {

            CircularProgressIndicator()

        } else {

            Button(

                onClick = {

                    isLoading = true

                    googleSignInLauncher.launch(
                        googleSignInClient.signInIntent
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)

            ) {

                Text(
                    text = "Continuar con Google",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        TextButton(
            onClick = onNavigateToRegister
        ) {

            Text(
                text = "¿No tienes cuenta? Regístrate"
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Al continuar aceptas nuestros términos y condiciones.",
            style = MaterialTheme.typography.bodySmall
        )
    }

}
