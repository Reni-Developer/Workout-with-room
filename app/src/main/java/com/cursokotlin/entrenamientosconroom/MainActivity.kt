package com.cursokotlin.entrenamientosconroom

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cursokotlin.entrenamientosconroom.data.firebase.AnalyticsService
import com.cursokotlin.entrenamientosconroom.ui.NavigatorWorkout
import com.cursokotlin.entrenamientosconroom.ui.screenHome.HomeViewModel
import com.cursokotlin.entrenamientosconroom.ui.theme.EntrenamientosConRoomTheme
import com.cursokotlin.entrenamientosconroom.ui.screenLogin.LoginViewModel
import com.cursokotlin.entrenamientosconroom.ui.screenUser.TrainingViewModel
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
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         installSplashScreen()
        enableEdgeToEdge()
        setContent {
            EntrenamientosConRoomTheme {
                    NavigatorWorkout(
                        trainingViewModel = trainingViewModel,
                        loginViewModel = loginViewModel,
                        homeViewModel = homeViewModel,
                        auth = auth
                    )
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
            loginViewModel.goScreenUser()
        }
    }
}