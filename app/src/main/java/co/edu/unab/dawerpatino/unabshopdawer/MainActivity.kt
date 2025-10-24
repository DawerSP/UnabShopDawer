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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnabShopDawerTheme {
                LoginScreen()


                val navController = rememberNavController()
                var startDestination = "login"
                val auth = Firebase.auth
                val currentUser = auth.currentUser

                if(currentUser != null){
                    startDestination = "home"
                }else{
                    startDestination = "login"
                }
                NavHost(navController, startDestination) {
                    composable(route = "login") {
                        LoginScreen(onClickRegister = {
                            navController.navigate("register")
                        }, onSuccessfulLogin = {
                            navController.navigate("home"){
                                popUpTo("login"){inclusive = true}
                            }
                        })
                    }
                    composable(route = "Register") {
                        RegisterScreen(onClickBack = {
                            navController.popBackStack()
                        }, onSuccesfulRegister = {
                            navController.navigate("home"){
                                popUpTo(0)

                            }
                        })
                    }
                    composable(route = "Home") {
                        HomeScreen(onClickLogout = {
                            navController.navigate("login"){
                                popUpTo(0)
                            }
                        })
                    }
                }
            }



        }
    }
}

