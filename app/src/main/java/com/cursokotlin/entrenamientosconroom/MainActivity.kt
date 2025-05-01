package com.cursokotlin.entrenamientosconroom

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.credentials.CredentialManager
import com.cursokotlin.entrenamientosconroom.data.firebase.AnalyticsService
import com.cursokotlin.entrenamientosconroom.ui.NavigatorWorkout
import com.cursokotlin.entrenamientosconroom.ui.theme.EntrenamientosConRoomTheme
import com.cursokotlin.entrenamientosconroom.ui.viewmodel.LoginViewModel
import com.cursokotlin.entrenamientosconroom.ui.viewmodel.TrainingViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var analyticsService: AnalyticsService

    private val loginViewModel: LoginViewModel by viewModels()
    private val trainingViewModel: TrainingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         installSplashScreen()

        enableEdgeToEdge()
        setContent {
            EntrenamientosConRoomTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigatorWorkout(
                        modifier = Modifier.padding(innerPadding),
                        trainingViewModel = trainingViewModel,
                        loginViewModel = loginViewModel,
                        auth = auth
                    )
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        analyticsService.logEvent(
            eventName = "InitApp",
            params = mapOf(
                "message" to "Integración de Firebase completa"
            )
        )
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("LoginMainActivity", "Cuenta logeada previamente.")
            loginViewModel.signIn()
        }
    }
}