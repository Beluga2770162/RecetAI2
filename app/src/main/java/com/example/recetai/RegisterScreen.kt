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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onNavigateToLogin: () -> Unit) {
    // ESTADOS: Aquí guardamos lo que César escriba en el Xiaomi
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)) // Slate 900
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Únete a RecetAI", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Crea tu cuenta para guardar recetas", color = Color.Gray, fontSize = 14.sp)

        Spacer(Modifier.height(32.dp))

        // CAMPO: NOMBRE
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre Completo") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF4ADE80), unfocusedLabelColor = Color.Gray)
        )

        Spacer(Modifier.height(16.dp))

        // CAMPO: EMAIL
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // CAMPO: PASSWORD
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // CAMPO: CONFIRMAR PASSWORD
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.height(32.dp))

        // BOTÓN: REGISTRARSE (VALIDACIÓN BÁSICA)
        Button(
            onClick = {
                if (password != confirmPassword) {
                    errorMessage = "Las contraseñas no coinciden"
                } else if (email.isBlank() || name.isBlank()) {
                    errorMessage = "Por favor llena todos los campos"
                } else {
                    errorMessage = ""
                    onRegisterSuccess() // Aquí luego conectarás con Firebase
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) {
            Text("Crear Cuenta", color = Color.White)
        }

        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión", color = Color(0xFF4ADE80))
        }
    }
}