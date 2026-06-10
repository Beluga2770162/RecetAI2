package com.example.recetai.ui.auth

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recetai.R

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    authViewModel: AuthViewModel = viewModel() // Inyectamos el ViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Estados para errores localizados
    var localErrorResId by remember { mutableStateOf<Int?>(null) }

    // Observamos los estados del ViewModel
    val isVmLoading by authViewModel.isLoading.collectAsState()
    val vmErrorResId by authViewModel.errorMessage.collectAsState()

    val isLoading = isVmLoading
    val displayError = localErrorResId ?: vmErrorResId

    // Estados para visibilidad de contraseñas
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    fun ejecutarRegistro() {
        localErrorResId = null
        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            localErrorResId = R.string.error_fill_fields
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            localErrorResId = R.string.invalid_email_error
            return
        }
        if (password != confirmPassword) {
            localErrorResId = R.string.error_passwords_mismatch
            return
        }
        if (password.length < 6) {
            localErrorResId = R.string.error_password_length
            return
        }

        // Delegamos el registro y la creación del perfil en Firestore al ViewModel
        authViewModel.registerWithEmail(
            email = email.trim(),
            clave = password.trim(),
            nombre = name.trim(),
            onSuccess = onRegisterSuccess
        )
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

            Icon(Icons.Default.PersonAdd, null, Modifier.size(76.dp), MaterialTheme.colorScheme.primary)
            Text(stringResource(id = R.string.join_app_title), fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text(stringResource(id = R.string.create_account_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(36.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it; localErrorResId = null },
                label = { Text(stringResource(id = R.string.full_name_label)) },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; localErrorResId = null },
                label = { Text(stringResource(id = R.string.email_label)) },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(14.dp))

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
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; localErrorResId = null },
                label = { Text(stringResource(id = R.string.confirm_password_label)) },
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                    }
                },
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { ejecutarRegistro() })
            )

            displayError?.let { errorId ->
                Text(
                    text = stringResource(id = errorId),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Button(onClick = { ejecutarRegistro() }, Modifier.fillMaxWidth().height(50.dp), enabled = !isLoading) {
                Text(stringResource(id = R.string.create_account_button))
            }
            TextButton(onClick = onNavigateToLogin, enabled = !isLoading) {
                Text(stringResource(id = R.string.already_have_account))
            }
        }

        if (isLoading) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)) {
                Box(contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            }
        }
    }
}