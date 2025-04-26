package com.cursokotlin.entrenamientosconroom.ui

import android.icu.number.Scale
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.sp
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
        Modifier.fillMaxWidth().padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(10.dp))
        Box(
            Modifier
                .background(Color.Black)
                .clickable { navigatorController.navigate(Routes.Screen2.route) }
                .align(Alignment.End)
                .padding(end = 8.dp)
        ) { Text(text = "TEST", color = Color.White, fontSize = 18.sp) }

        Spacer(Modifier.size(80.dp))
        IncreaseFit()

        Spacer(Modifier.size(100.dp))

        EmailField(email, password, loginViewModel)
        Spacer(Modifier.size(4.dp))
        PasswordField(password, email, loginViewModel)
        Spacer(Modifier.size(4.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SignIn()
            Spacer(Modifier.size(20.dp))
            LoginGoogle()
            Spacer(Modifier.size(10.dp))
            LoginFacebook()
        }


        Spacer(Modifier.size(100.dp))
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Login(Modifier.weight(1f), enableCreateAccount, loginViewModel)
            Spacer(Modifier.size(4.dp))
            Or()
            Spacer(Modifier.size(4.dp))
            CreateAccount(Modifier.weight(1f), enableCreateAccount, loginViewModel)

        }

    }
}

@Composable
fun IncreaseFit() {
    Image(
        painter = painterResource
            (id = R.drawable.logo),
        contentDescription = "Logo increase Fit",
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun EmailField(email: String, password: String, loginViewModel: LoginViewModel) {

    TextField(
        value = email,
        onValueChange = { loginViewModel.changeEmPwLgS(it, password) },
        modifier = Modifier,
        label = { Text(text = "Email", fontSize = 20.sp) },
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
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = Color(0xAB394881),
            unfocusedLabelColor = Color(0xFF436AFB)
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
        label = { Text(text = "Password", fontSize = 20.sp) },
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
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = Color(0xAB394881),
            unfocusedLabelColor = Color(0xFF436AFB)
        )
    )
}

@Composable
fun SignIn() {
    Text(text = "Sign In with:", color = Color(0xFFA2A2A5))
}

@Composable
fun LoginGoogle() {
    Image(
        painter = painterResource(id = R.drawable.logo_google), contentDescription = "Google",
        Modifier.size(40.dp)
    )
}

@Composable
fun LoginFacebook() {
    Image(
        painter = painterResource(id = R.drawable.logo_facebook), contentDescription = "Facebook",
        Modifier.size(32.dp)
    )
}

@Composable
fun Login(modifier: Modifier, enableCreateAccount: Boolean, loginViewModel: LoginViewModel) {
    Button(
        onClick = { },
        enabled = enableCreateAccount,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF446BFD),
            contentColor = Color(0xFFFFFFFF),
            disabledContainerColor = Color(0xFF899DB4),
            disabledContentColor = Color(0xFFCBC9C9)
        ),
        elevation = ButtonDefaults.buttonElevation(4.dp)
    ) {
        Text(text = "Login", fontSize = 23.sp)
    }
}

@Composable
fun Or() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp),
            thickness = 0.5.dp,
            color = Color(0xFFA2A2A5)
        )
        Text(text = "OR", color = Color(0xFFA2A2A5))
        Divider(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp),
            thickness = 0.5.dp,
            color = Color(0xFFA2A2A5)
        )
    }
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
            contentColor = Color(0xFFFFFFFF),
            disabledContainerColor = Color(0xFF899DB4),
            disabledContentColor = Color(0xFFCBC9C9)
        ),
        elevation = ButtonDefaults.buttonElevation(4.dp)
    ) {
        Text(text = "Create Account", fontSize = 23.sp)
    }
}