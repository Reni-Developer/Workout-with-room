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
import com.cursokotlin.entrenamientosconroom.ui.theme.EntrenamientosConRoomTheme
import com.cursokotlin.entrenamientosconroom.ui.TrainingScreen
import com.cursokotlin.entrenamientosconroom.ui.viewmodel.TrainingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

private val trainingViewModel: TrainingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EntrenamientosConRoomTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TrainingScreen(modifier = Modifier.padding(innerPadding), trainingViewModel = trainingViewModel)
                }
            }
        }
    }
}
