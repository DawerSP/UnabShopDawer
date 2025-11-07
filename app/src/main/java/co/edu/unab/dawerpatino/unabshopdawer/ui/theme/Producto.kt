package co.edu.unab.dawerpatino.unabshopdawer.ui.theme

// Data class corregida para ser compatible con Firebase Firestore
data class Producto(
    val id: String = "", // Cambiado de String? a String con valor por defecto
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0
)
