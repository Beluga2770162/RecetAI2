package com.example.recetai.ui.inventory

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InventoryViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var inventoryListener: ListenerRegistration? = null

    private val _inventory = MutableStateFlow<List<InventoryItem>>(emptyList())
    val inventory: StateFlow<List<InventoryItem>> = _inventory.asStateFlow()

    init {
        startInventorySnapshotListener()
    }

    private fun startInventorySnapshotListener() {
        val uid = auth.currentUser?.uid ?: return

        inventoryListener = db.collection("inventory")
            .whereEqualTo("userId", uid)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.e("RECETAI", "Error en escucha del inventario", error)
                    return@addSnapshotListener
                }

                snapshots?.let { snapshot ->
                    // Mapeo directo y seguro de documentos a objetos
                    val items = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(InventoryItem::class.java)?.copy(id = doc.id)
                    }
                    _inventory.value = items
                }
            }
    }

    fun addIngredient(ingredientName: String) {
        val uid = auth.currentUser?.uid ?: return

        // Creamos el objeto sin ID manualmente, dejaremos que Firestore genere el ID
        // o podemos usar el método .add() para que Firebase cree el documento
        val newItem = InventoryItem(
            userId = uid,
            name = ingredientName,
            category = "General",
            detectedByAI = false
        )

        db.collection("inventory").add(newItem)
            .addOnFailureListener { Log.e("RECETAI", "Error al guardar ingrediente", it) }
    }

    fun deleteIngredient(itemId: String) {
        if (itemId.isBlank()) return
        db.collection("inventory").document(itemId).delete()
            .addOnFailureListener { Log.e("RECETAI", "Error al borrar ingrediente", it) }
    }

    override fun onCleared() {
        super.onCleared()
        inventoryListener?.remove()
    }
}