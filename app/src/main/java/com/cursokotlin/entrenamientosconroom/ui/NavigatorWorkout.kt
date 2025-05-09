package com.cursokotlin.entrenamientosconroom.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cursokotlin.entrenamientosconroom.R
import com.cursokotlin.entrenamientosconroom.ui.screenHome.HomeScreen
import com.cursokotlin.entrenamientosconroom.ui.screenHome.HomeViewModel
import com.cursokotlin.entrenamientosconroom.ui.screenLogin.LoginScreen
import com.cursokotlin.entrenamientosconroom.ui.screenLogin.LoginViewModel
import com.cursokotlin.entrenamientosconroom.ui.screenUser.TrainingScreen
import com.cursokotlin.entrenamientosconroom.ui.screenUser.TrainingViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigatorWorkout(
    trainingViewModel: TrainingViewModel,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    auth: FirebaseAuth
) {

    val navigatorController = rememberNavController()
    val destination by loginViewModel.navigator.observeAsState(initial = Navigator.Screen1)

    LaunchedEffect(destination) {
        when (destination) {
            is Navigator.Screen2 -> {
                navigatorController.navigate(Routes.Screen2.route) {
                    popUpTo(0) { inclusive = true }// Para limpiar stack
                }
            }

            is Navigator.Screen1 -> {
                navigatorController.navigate(Routes.Screen1.route) {
                    popUpTo(0) { inclusive = true }
                }
            }

            is Navigator.Screen3 -> {
                navigatorController.navigate(Routes.Screen3.route) {
                    popUpTo(0) { inclusive = true }
                }
            }

            is Navigator.Screen4 -> {
                navigatorController.navigate(Routes.Screen4.route) {
                    popUpTo(0) { inclusive = true }
                }
            }

            is Navigator.Screen5 -> {
                navigatorController.navigate(Routes.Screen5.route) {
                    popUpTo(0) { inclusive = true }
                }
            }

            is Navigator.Screen6 -> {
                navigatorController.navigate(Routes.Screen6.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (destination != Navigator.Screen1) {
                NavigatorBar(loginViewModel)
            }
        }) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navigatorController,
            startDestination =
                if (auth.currentUser != null) {
                    Routes.Screen2.route
                } else {
                    Routes.Screen1.route
                }
        ) {
            composable(route = Routes.Screen1.route) {
                LoginScreen(loginViewModel)
            }
            composable(route = Routes.Screen2.route) {
                TrainingScreen(loginViewModel, trainingViewModel, homeViewModel)
            }
            composable(route = Routes.Screen3.route) {
                HomeScreen(trainingViewModel, homeViewModel)
            }
        }
    }
}

sealed class Routes(val route: String) {
    object Screen1 : Routes("Screen1")
    object Screen2 : Routes("Screen2")
    object Screen3 : Routes("Screen3")
    object Screen4 : Routes("Screen4")
    object Screen5 : Routes("Screen5")
    object Screen6 : Routes("Screen6")
}

sealed class Navigator {
    object Screen1 : Navigator()
    object Screen2 : Navigator()
    object Screen3 : Navigator()
    object Screen4 : Navigator()
    object Screen5 : Navigator()
    object Screen6 : Navigator()
}

@Composable
fun NavigatorBar(loginViewModel: LoginViewModel) {

    val destination by loginViewModel.navigator.observeAsState(initial = Navigator.Screen1)

    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(colorResource(id = R.color.unselected))
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            ItemBarNav(
                icon = { R.drawable.ic_bar_home_icon },
                description = "home",
                Modifier
                    .weight(1f)
                    .size(40.dp)
                    .clickable { loginViewModel.goScreenHome() },
                tint = if (destination == Navigator.Screen3) Color.Blue
                else Color.Black
            )

            ItemBarNav(
                icon = { R.drawable.ic_bar_note_icon },
                description = "note",
                Modifier
                    .weight(1f)
                    .size(36.dp)
                    .clickable { loginViewModel.goScreenUser() },
                tint = if (destination == Navigator.Screen4) Color.Blue
                 else  Color.Black
            )

            ItemBarNav(
                icon = { R.drawable.ic_bar_head_icon },
                description = "head",
                Modifier
                    .weight(1f)
                    .size(60.dp)
                    .clickable { loginViewModel.goScreenUser() },
                tint = if (destination == Navigator.Screen5)
                    Color.Blue else Color.Black
            )

            ItemBarNav(
                icon = { R.drawable.ic_bar_calendar_icon },
                description = "calendar",
                Modifier
                    .weight(1f)
                    .size(40.dp)
                    .clickable { loginViewModel.goScreenUser() },
                tint = if (destination == Navigator.Screen6) Color.Blue
                else Color.Black
            )

            ItemBarNav(
                icon = { R.drawable.ic_bar_user_icon },
                description = "user",
                Modifier
                    .weight(1f)
                    .size(40.dp)
                    .clickable { loginViewModel.goScreenUser() },
                tint = if (destination == Navigator.Screen2) Color.Blue
                else Color.Black
            )
        }
    }
}

@Composable
fun ItemBarNav(icon: () -> Int, description: String, modifier: Modifier, tint: Color) {
    Icon(
        painter = painterResource(icon()),
        contentDescription = description,
        modifier = modifier,
        tint = tint
    )
}