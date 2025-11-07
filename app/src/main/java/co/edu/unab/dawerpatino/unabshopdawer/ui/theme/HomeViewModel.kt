package co.edu.unab.dawerpatino.unabshopdawer.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    init {
        // Esta función ya obtiene los productos en tiempo real, así que no necesitamos más
        obtenerProductosEnTiempoReal()
    }

    // Cambié el nombre para que sea más claro lo que hace
    private fun obtenerProductosEnTiempoReal() {
        viewModelScope.launch {
            db.collection("productos").addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Error al obtener productos: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val listaProductos = snapshot.documents.mapNotNull { document ->
                        val producto = document.toObject(Producto::class.java)
                        producto?.copy(id = document.id)
                    }
                    _productos.value = listaProductos
                }
            }
        }
    }

    // --- FUNCIÓN NUEVA PARA AÑADIR PRODUCTOS ---
    fun agregarProducto(producto: Producto, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        // Usamos una copia sin el ID, ya que Firestore lo genera automáticamente
        val productoParaGuardar = hashMapOf(
            "nombre" to producto.nombre,
            "descripcion" to producto.descripcion,
            "precio" to producto.precio
        )

        db.collection("productos")
            .add(productoParaGuardar) // Usamos el objeto sin ID
            .addOnSuccessListener {
                println("¡Producto agregado con éxito!")
                onSuccess() // Llama a la función de éxito (ej. para navegar hacia atrás)
            }
            .addOnFailureListener { e ->
                println("Error al agregar producto: $e")
                onFailure(e) // Llama a la función de fallo
            }
    }
}
