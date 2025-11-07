package co.edu.unab.dawerpatino.unabshopdawer.ui.theme


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import co.edu.unab.dawerpatino.unabshopdawer.ui.theme.FirestoreRepository
import co.edu.unab.dawerpatino.unabshopdawer.ui.theme.Producto

class HomeViewModel : ViewModel() {
    private val repo = FirestoreRepository()
    var productos = mutableStateListOf<Producto>()

    fun cargarProductos() {
        repo.obtenerProductos { lista ->
            productos.clear()
            productos.addAll(lista)
        }
    }

    fun agregarProducto(nombre: String, descripcion: String, precio: Double) {
        val nuevo = Producto(nombre = nombre, descripcion = descripcion, precio = precio)
        repo.agregarProducto(nuevo) {
            if (it) cargarProductos()
        }
    }

    fun eliminarProducto(id: String) {
        repo.eliminarProducto(id) {
            if (it) cargarProductos()
        }
    }
}