package com.cursokotlin.entrenamientosconroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.cursokotlin.entrenamientosconroom.ui.NavigatorWorkout
import com.cursokotlin.entrenamientosconroom.ui.theme.EntrenamientosConRoomTheme
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
        super.onCreate(savedInstanceState)

        // Analitycs Events
        firebaseAnalytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completa")
        firebaseAnalytics.logEvent("initApp", bundle)

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