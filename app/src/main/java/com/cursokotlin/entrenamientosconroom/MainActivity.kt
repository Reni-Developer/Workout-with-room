package com.cursokotlin.entrenamientosconroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cursokotlin.entrenamientosconroom.ui.LoginScreen
import com.cursokotlin.entrenamientosconroom.ui.theme.EntrenamientosConRoomTheme
import com.cursokotlin.entrenamientosconroom.ui.TrainingScreen
import com.cursokotlin.entrenamientosconroom.ui.viewmodel.LoginViewModel
import com.cursokotlin.entrenamientosconroom.ui.viewmodel.TrainingViewModel
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val trainingViewModel: TrainingViewModel by viewModels()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {

        // Analitycs Events
        firebaseAnalytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completa")
        firebaseAnalytics.logEvent("initApp", bundle)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EntrenamientosConRoomTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigatorWorkout(
                        modifier = Modifier.padding(innerPadding),
                        trainingViewModel = trainingViewModel,
                        loginViewModel = loginViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun NavigatorWorkout(
    modifier: Modifier = Modifier,
    trainingViewModel: TrainingViewModel,
    loginViewModel: LoginViewModel
) {

    val navigatorController = rememberNavController()

    NavHost(
        navController = navigatorController,
        startDestination = Routes.Screen1.route,
        modifier = modifier
    ) {
        composable(route = Routes.Screen1.route) {
            LoginScreen(
                modifier,
                navigatorController,
                loginViewModel
            )
        }
        composable(route = Routes.Screen2.route) {
            TrainingScreen(
                modifier,
                navigatorController,
                trainingViewModel
            )
        }
    }
}

sealed class Routes(val route: String) {
    object Screen1 : Routes("Screen1")
    object Screen2 : Routes("Screen2")
}