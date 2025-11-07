package co.edu.unab.dawerpatino.unabshopdawer.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClickLogout: () -> Unit = {},
    onNavigateToAddProduct: () -> Unit, // <-- PARÁMETRO NUEVO para navegar
    homeViewModel: HomeViewModel = viewModel()
) {
    val auth = Firebase.auth
    val user = auth.currentUser
    val productos by homeViewModel.productos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unab Shop") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFA726), titleContentColor = Color.White, actionIconContentColor = Color.White),
                actions = {
                    IconButton(onClick = { /* TODO: Navegar a la pantalla del carrito */ }) {
                        Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Carrito de compras")
                    }
                    IconButton(onClick = { auth.signOut(); onClickLogout() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        // --- BOTÓN FLOTANTE PARA AÑADIR PRODUCTOS ---
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddProduct) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Producto")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            user?.email?.let { Text(text = "Bienvenido, $it", modifier = Modifier.padding(bottom = 16.dp)) }

            if (productos.isEmpty()) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No hay productos disponibles. ¡Agrega uno nuevo!")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(productos) { producto ->
                        ProductoCard(producto = producto) // El Card ahora tiene el botón
                    }
                }
            }
        }
    }
}

// --- TARJETA DEL PRODUCTO ACTUALIZADA ---
@Composable
fun ProductoCard(producto: Producto) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = producto.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = producto.descripcion, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$${"%.2f".format(producto.precio)}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                // --- BOTÓN PARA AÑADIR AL CARRITO ---
                Button(onClick = { /* TODO: Lógica para agregar al carrito */ }) {
                    Text("Añadir al carrito")
                }
            }
        }
    }
}

// --- PREVIEW ACTUALIZADO ---
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    UnabShopDawerTheme {
        // En el preview, la navegación no hace nada
        HomeScreen(onNavigateToAddProduct = {})
    }
}
