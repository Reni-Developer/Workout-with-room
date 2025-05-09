package com.cursokotlin.entrenamientosconroom.ui.screenLogin

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cursokotlin.entrenamientosconroom.R

@Composable
fun LoginScreen(loginViewModel: LoginViewModel) {

    val email: String by loginViewModel.email.collectAsState(initial = "reni@prueba.com")
    val password: String by loginViewModel.password.collectAsState(initial = "*Reni1234")
    val enableCreateAccount by loginViewModel.buttonEnabled.collectAsState(false)
    val stateFirstSignIn by loginViewModel.stateFirstSignIn.collectAsState(true)
    val errorType by loginViewModel.errorType.collectAsState(99)

    val activity = LocalActivity.current

    val textError0 =
        "''La cuenta a la que intenta acceder no está registrada. Por favor, inicie sesión " +
                "con una cuenta existente o cree una nueva si aún no dispone de una. Asegúrese " +
                "de que el correo electrónico esté en un formato válido (por ejemplo, " +
                "usuario@dominio.com) y que la contraseña contenga al menos 8 caracteres, " +
                "incluyendo una letra mayúscula, un número y un símbolo especial.''"

    val textError1 =
        "''La cuenta que intenta crear ya ha sido registrada. Por favor, inicie sesión " +
                "con una cuenta existente o utilice una dirección de correo diferente para " +
                "crear una nueva. Asegúrese de que el correo electrónico esté en un formato " +
                "válido (por ejemplo, usuario@dominio.com) y que la contraseña contenga al " +
                "menos 8 caracteres, incluyendo una letra mayúscula, un número y un símbolo especial.''"

    val textErrorG = "No se pudo autenticar tu cuenta de Google. Por favor, verifica que hayas " +
            "seleccionado la cuenta correcta e intenta nuevamente."

    val textErrorC = "Autenticación con credenciales fallidas. Por favor, verifica que hayas " +
            "seleccionado la cuenta correcta e intenta nuevamente."

    Column(
        Modifier
            .fillMaxWidth()
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(100.dp))
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
            LoginGoogle(loginViewModel, activity)
            Spacer(Modifier.size(15.dp))
            LoginFacebook()
        }

        Spacer(Modifier.size(100.dp))
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Login(enableCreateAccount, loginViewModel)
            Spacer(Modifier.size(4.dp))
            Or()
            Spacer(Modifier.size(4.dp))
            CreateAccount(enableCreateAccount, loginViewModel)

            when (errorType) {
                0 -> ErrorDialog(textError0, loginViewModel)
                1 -> ErrorDialog(textError1, loginViewModel)
                2 -> ErrorDialog(textErrorG, loginViewModel)
                3 -> ErrorDialog(textErrorC, loginViewModel)
            }
        }
    }
    if (stateFirstSignIn){
        loginViewModel.firstSignIn(activity)
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
            .padding(horizontal = 30.dp),
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
fun LoginGoogle(loginViewModel: LoginViewModel, activity: Activity?) {
    Image(
        painter = painterResource(id = R.drawable.logo_google), contentDescription = "Google",
        Modifier
            .size(40.dp)
            .clickable { loginViewModel.signInWithGoogleButton(activity) }
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
fun Login(
    enableCreateAccount: Boolean,
    loginViewModel: LoginViewModel
) {
    Button(
        onClick = { loginViewModel.onEmailAndPassSignIn() },
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
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp),
            thickness = 0.8.dp,
            color = Color(0xFFA2A2A5)
        )
        Text(text = "OR", color = Color(0xFFA2A2A5))
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp),
            thickness = 0.8.dp,
            color = Color(0xFFA2A2A5)
        )
    }
}

@Composable
fun CreateAccount(
    enableCreateAccount: Boolean,
    loginViewModel: LoginViewModel
) {

    Button(
        onClick = { loginViewModel.onCreateAccount() },
        enabled = enableCreateAccount,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF686B79),
            contentColor = Color(0xFFFFFFFF),
            disabledContainerColor = Color(0xFF899DB4),
            disabledContentColor = Color(0xFFCBC9C9)
        ),
        elevation = ButtonDefaults.buttonElevation(4.dp)
    ) {
        Text(text = "Create Account", fontSize = 23.sp)
    }
}

@Composable
fun ErrorDialog(textError: String, loginViewModel: LoginViewModel) {
    AlertDialog(
        onDismissRequest = { loginViewModel.confirmButton() },
        confirmButton = {
            Button(
                onClick = { loginViewModel.confirmButton() },
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF793939)),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text(text = "ACCEPT", color = Color.White)
            }
        },
        icon = {
            Image(
                painter = painterResource(id = R.drawable.error_icon),
                contentDescription = "Icon Error",
                modifier = Modifier.size(100.dp)
            )
        },
        title = { Text(text = "ERROR", color = Color.Red, fontWeight = FontWeight.ExtraBold) },
        text = {
            Text(
                text = textError,
                color = Color.DarkGray,
                fontSize = 16.sp,
                textAlign = TextAlign.Justify
            )
        }
    )
}