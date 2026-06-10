package com.example.recetai.ui.auth

import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recetai.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Usamos Int? para manejar los errores de validación locales con strings.xml
    var localErrorResId by remember { mutableStateOf<Int?>(null) }

    // Observamos los estados del ViewModel
    val isVmLoading by authViewModel.isLoading.collectAsState()
    val vmErrorResId by authViewModel.errorMessage.collectAsState()

    val isLoading = isVmLoading
    // Priorizamos el error local (campos vacíos) sobre el del ViewModel
    val displayError = localErrorResId ?: vmErrorResId

    val isEmailValid = email.isBlank() || Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()

    val webClientId = remember { context.getString(context.resources.getIdentifier("default_web_client_id", "string", context.packageName)) }
    val gso = remember { GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(webClientId).requestEmail().build() }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val googleLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            authViewModel.signInWithGoogle(credential, onLoginSuccess)

        } catch (e: Exception) {
            localErrorResId = R.string.error_login_cancelled
        }
    }

    fun ejecutarLogin() {
        localErrorResId = null
        if (email.isBlank() || password.isBlank()) {
            localErrorResId = R.string.error_fill_fields
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            localErrorResId = R.string.invalid_email_error
            return
        }

        // Delegamos la lógica al ViewModel
        authViewModel.signInWithEmail(email.trim(), password.trim(), onLoginSuccess)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Restaurant, null, Modifier.size(86.dp), MaterialTheme.colorScheme.primary)
            Text(stringResource(id = R.string.app_name), fontSize = 36.sp, fontWeight = FontWeight.Bold)
            Text(stringResource(id = R.string.app_slogan), color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; localErrorResId = null },
                label = { Text(stringResource(id = R.string.email_label)) },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                modifier = Modifier.fillMaxWidth(),
                isError = !isEmailValid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; localErrorResId = null },
                label = { Text(stringResource(id = R.string.password_label)) },
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { ejecutarLogin() })
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onNavigateToForgotPassword) {
                    Text(stringResource(id = R.string.forgot_password_prompt))
                }
            }

            // Mostrar el error traducido
            displayError?.let { errorId ->
                Text(
                    text = stringResource(id = errorId),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Button(
                onClick = { ejecutarLogin() },
                Modifier.fillMaxWidth().height(50.dp),
                enabled = !isLoading
            ) {
                Text(stringResource(id = R.string.login_button))
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    localErrorResId = null
                    googleLauncher.launch(googleSignInClient.signInIntent)
                },
                Modifier.fillMaxWidth().height(50.dp),
                enabled = !isLoading
            ) {
                Text(stringResource(id = R.string.continue_with_google))
            }

            TextButton(onClick = onNavigateToRegister) {
                Text(stringResource(id = R.string.no_account_register))
            }
        }

        if (isLoading) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)) {
                Box(contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            }
        }
    }
}