package com.cursokotlin.entrenamientosconroom.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cursokotlin.entrenamientosconroom.ui.viewmodel.LoginViewModel
import com.cursokotlin.entrenamientosconroom.ui.viewmodel.TrainingViewModel

@Composable
fun NavigatorWorkout(
    modifier: Modifier = Modifier,
    trainingViewModel: TrainingViewModel,
    loginViewModel: LoginViewModel
) {

    val navigatorController = rememberNavController()
    val destination by loginViewModel.navigator.observeAsState(initial = Navigator.Screen1)

    LaunchedEffect(destination) {
        when (destination) {
            is Navigator.Screen2 -> {
                navigatorController.navigate(Routes.Screen2.route) {
                    popUpTo(Routes.Screen1.route) { inclusive = true }// Para limpiar stack
                }
            }

            is Navigator.Screen1 -> {
                navigatorController.navigate(Routes.Screen1.route) {
                    popUpTo(Routes.Screen2.route) { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navigatorController,
        startDestination = Routes.Screen1.route,
        modifier = modifier
    ) {
        composable(route = Routes.Screen1.route) {
            LoginScreen(loginViewModel)
        }
        composable(route = Routes.Screen2.route) {
            TrainingScreen(modifier, loginViewModel, trainingViewModel)
        }
    }
}

sealed class Routes(val route: String) {
    object Screen1 : Routes("Screen1")
    object Screen2 : Routes("Screen2")
}

sealed class Navigator {
    object Screen1 : Navigator()
    object Screen2 : Navigator()
}