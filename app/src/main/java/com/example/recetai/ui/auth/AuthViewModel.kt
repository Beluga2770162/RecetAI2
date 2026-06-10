package com.example.recetai.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.recetai.R // <-- Importante: Importa tus recursos
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Cambiamos a Int para emitir el ID del recurso (R.string...)
    private val _errorMessage = MutableStateFlow<Int?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // --- GOOGLE SIGN IN ---
    fun signInWithGoogle(credential: AuthCredential, onSuccess: () -> Unit) {
        _isLoading.value = true
        _errorMessage.value = null

        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                guardarUsuarioEnFirestore(authResult.user, null, onSuccess)
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                Log.e("AuthViewModel", "Error Google: ${e.localizedMessage}")
                _errorMessage.value = R.string.error_google_signin // Reemplazo multilingüe
            }
    }

    // --- LOGIN CON CORREO ---
    fun signInWithEmail(email: String, clave: String, onSuccess: () -> Unit) {
        _isLoading.value = true
        _errorMessage.value = null

        auth.signInWithEmailAndPassword(email, clave)
            .addOnSuccessListener { authResult ->
                guardarUsuarioEnFirestore(authResult.user, null, onSuccess)
            }
            .addOnFailureListener {
                _isLoading.value = false
                _errorMessage.value = R.string.error_invalid_credentials // Reemplazo multilingüe
            }
    }

    // --- REGISTRO CON CORREO ---
    fun registerWithEmail(email: String, clave: String, nombre: String, onSuccess: () -> Unit) {
        _isLoading.value = true
        _errorMessage.value = null

        auth.createUserWithEmailAndPassword(email, clave)
            .addOnSuccessListener { authResult ->
                guardarUsuarioEnFirestore(authResult.user, nombre, onSuccess)
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                Log.e("AuthViewModel", "Error Registro: ${e.localizedMessage}")
                _errorMessage.value = R.string.error_create_account // Reemplazo multilingüe
            }
    }

    // --- SINCRONIZACIÓN CON FIRESTORE ---
    private fun guardarUsuarioEnFirestore(usuario: FirebaseUser?, nombreManual: String?, onSuccess: () -> Unit) {
        if (usuario == null) {
            _isLoading.value = false
            _errorMessage.value = R.string.error_null_user
            return
        }

        // Dejaremos el nombre por defecto vacío si no hay uno,
        // para que la UI decida si muestra "Chef" o "Chefe" (PT) leyendo el stringResource
        val displayName = nombreManual ?: usuario.displayName ?: ""

        val userData = hashMapOf(
            "uid" to usuario.uid,
            "name" to displayName,
            "email" to usuario.email,
            "photoUrl" to usuario.photoUrl?.toString(),
            "lastLogin" to System.currentTimeMillis()
        )

        db.collection("users").document(usuario.uid)
            .set(userData, SetOptions.merge())
            .addOnSuccessListener {
                _isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                Log.e("AuthViewModel", "Error guardando en Firestore", e)
                _errorMessage.value = R.string.error_sync_profile // Reemplazo multilingüe
            }
    }

    fun signOut(onSignOutComplete: () -> Unit) {
        auth.signOut()
        onSignOutComplete()
    }
}