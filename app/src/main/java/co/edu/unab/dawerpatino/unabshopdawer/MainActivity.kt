package co.edu.unab.dawerpatino.unabshopdawer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// IMPORTACIONES NUEVAS Y NECESARIAS
import co.edu.unab.dawerpatino.unabshopdawer.ui.theme.AgregarProductoScreen
import co.edu.unab.dawerpatino.unabshopdawer.ui.theme.HomeScreen
import co.edu.unab.dawerpatino.unabshopdawer.ui.theme.LoginScreen
import co.edu.unab.dawerpatino.unabshopdawer.ui.theme.RegisterScreen
import co.edu.unab.dawerpatino.unabshopdawer.ui.theme.UnabShopDawerTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() es para usar la pantalla completa, está bien dejarlo.
        enableEdgeToEdge()
        setContent {
            UnabShopDawerTheme {
                // Surface es el contenedor principal de tu app.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llamamos a la función que contiene TODA la lógica de navegación.
                    AppNavigation()
                }
            }
        }
    }
}

/**
 * Composable que define y controla todo el grafo de navegación de la aplicación.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Lógica para decidir la pantalla de inicio:
    // Si el usuario ya está logueado, la app empieza en "home", si no, en "login".
    val startDestination = if (Firebase.auth.currentUser != null) {
        "home"
    } else {
        "login"
    }

    // NavHost es el componente que gestiona qué pantalla se muestra.
    NavHost(navController = navController, startDestination = startDestination) {

        // RUTA 1: Pantalla de Login
        composable(route = "login") {
            LoginScreen(
                onClickRegister = {
                    navController.navigate("register")
                },
                onSuccessfulLogin = {
                    // Al loguearse bien, vamos a "home" y limpiamos el historial para no volver a "login".
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // RUTA 2: Pantalla de Registro
        composable(route = "register") { // Corregido: "register" en minúsculas por convención
            RegisterScreen(
                onClickBack = {
                    navController.popBackStack() // Vuelve a la pantalla anterior (Login)
                },
                onSuccesfulRegister = {
                    // Al registrarse bien, vamos a "home" y limpiamos todo el historial anterior.
                    navController.navigate("home") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }

        // RUTA 3: Pantalla Principal (Home)
        composable(route = "home") { // Corregido: "home" en minúsculas por convención
            HomeScreen(
                onClickLogout = {
                    Firebase.auth.signOut() // Primero cerramos la sesión en Firebase
                    // Luego navegamos a "login" y limpiamos el historial para no volver a "home".
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                // --- ACCIÓN NUEVA ---
                onNavigateToAddProduct = {
                    // Cuando se presiona el botón '+', navegamos a la pantalla de agregar producto.
                    navController.navigate("agregar_producto")
                }
            )
        }

        // --- RUTA NUEVA ---
        // RUTA 4: Pantalla para Agregar Producto
        composable(route = "agregar_producto") {
            AgregarProductoScreen(
                onProductoAgregado = {
                    // Cuando el producto se guarda, volvemos a la pantalla anterior (Home).
                    navController.popBackStack()
                }
            )
        }
    }
}