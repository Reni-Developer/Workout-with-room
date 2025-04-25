package com.cursokotlin.entrenamientosconroom.ui

import android.icu.number.Scale
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cursokotlin.entrenamientosconroom.R
import com.cursokotlin.entrenamientosconroom.Routes
import com.cursokotlin.entrenamientosconroom.ui.viewmodel.LoginViewModel


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigatorController: NavHostController,
    loginViewModel: LoginViewModel
) {

    val email: String by loginViewModel.email.collectAsState(initial = "")
    val password: String by loginViewModel.password.collectAsState(initial = "")
    val enableCreateAccount by loginViewModel.buttonEnabled.collectAsState(false)

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            Modifier
                .background(Color.Blue)
                .clickable { navigatorController.navigate(Routes.Screen2.route) }
                .align(Alignment.End)) { Text(text = "TEST") }

        Spacer(Modifier.size(100.dp))
        IncreaseFit()

        Spacer(Modifier.size(100.dp))

        EmailField(email, password, loginViewModel)
        Spacer(Modifier.size(4.dp))
        PasswordField(password, email, loginViewModel)

        Spacer(Modifier.size(100.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            CreateAccount(Modifier.weight(1f), enableCreateAccount, loginViewModel)
            Spacer(Modifier.size(4.dp))
            Login(Modifier.weight(1f), enableCreateAccount, loginViewModel)
        }

    }
}

@Composable
fun IncreaseFit() {
    Image(
        painter = painterResource
            (id = R.drawable.logo),
        contentDescription = "Logo increase Fit",
        Modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun EmailField(email: String, password: String, loginViewModel: LoginViewModel) {

    TextField(
        value = email,
        onValueChange = { loginViewModel.changeEmPwLgS(it, password) },
        modifier = Modifier,
        label = { Text(text = "Email") },
        trailingIcon = {
            Image(
                imageVector = Icons.Filled.Email,
                contentDescription = "Email Icon",
                colorFilter = ColorFilter.tint(Color(0xFF446BFD)),
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF37383D),
            unfocusedTextColor = Color(0xE656575E),
            focusedContainerColor = Color(0xFFA3A3A6),
            unfocusedContainerColor = Color(0xFFDFE0EA),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun PasswordField(password: String, email: String, loginViewModel: LoginViewModel) {

    val passwordVisibility by loginViewModel.passwordVisibility.collectAsState(false)
    val image by loginViewModel.image.collectAsState(Icons.Filled.Visibility)

    TextField(
        value = password,
        onValueChange = { loginViewModel.changeEmPwLgS(email, it) },
        modifier = Modifier,
        label = { Text(text = "Password") },
        trailingIcon = {
            Icon(
                imageVector = image,
                contentDescription = "Visivility",
                Modifier.clickable {
                    loginViewModel.onPasswordVisibilityChanged(passwordVisibility)
                }
            )
        },
        visualTransformation =
            if (passwordVisibility == false)
                PasswordVisualTransformation()
            else {
                VisualTransformation.None
            },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF37383D),
            unfocusedTextColor = Color(0xE656575E),
            focusedContainerColor = Color(0xFFA3A3A6),
            unfocusedContainerColor = Color(0xFFDFE0EA),
            focusedIndicatorColor = Color(0xFF485C91),
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun CreateAccount(
    modifier: Modifier,
    enableCreateAccount: Boolean,
    loginViewModel: LoginViewModel
) {

    Button(
        onClick = { },
        enabled = enableCreateAccount,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF446BFD),
            contentColor = Color(0xFF000000),
            disabledContainerColor = Color(0xFF34373D),
            disabledContentColor = Color(0xFF252424)
        )
    ) {
        Text(text = "Create Account")
    }
}

@Composable
fun Login(modifier: Modifier, enableCreateAccount: Boolean, loginViewModel: LoginViewModel) {
    Button(
        onClick = { },
        enabled = enableCreateAccount,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF446BFD),
            contentColor = Color(0xFF000000),
            disabledContainerColor = Color(0xFF34373D),
            disabledContentColor = Color(0xFF252424)
        )
    ) {
        Text(text = "Login")
    }
}