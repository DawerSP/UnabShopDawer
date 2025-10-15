package co.edu.unab.dawerpatino.unabshopdawer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.edu.unab.dawerpatino.unabshopdawer.ui.theme.HomeScreen
import co.edu.unab.dawerpatino.unabshopdawer.ui.theme.LoginScreen
import co.edu.unab.dawerpatino.unabshopdawer.ui.theme.RegisterScreen
import co.edu.unab.dawerpatino.unabshopdawer.ui.theme.UnabShopDawerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val startDestination = "login"
            NavHost(navController, startDestination) {
                composable(route = "login") {
                    LoginScreen()
                }
                composable(route = "Register") {
                    RegisterScreen()
                }
                composable(route = "Home") {
                    HomeScreen()
                }
            }


            RegisterScreen()
        }
    }
}

