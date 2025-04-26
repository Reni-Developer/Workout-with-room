package com.cursokotlin.entrenamientosconroom.ui.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cursokotlin.entrenamientosconroom.ui.Navigator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _image = MutableStateFlow<ImageVector>(Icons.Filled.Visibility)
    val image: StateFlow<ImageVector> = _image

    private val _password = MutableStateFlow<String>("12345678")
    val password: StateFlow<String> = _password

    private val _email = MutableStateFlow<String>("reni@prueba.com")
    val email: StateFlow<String> = _email

    private val _passwordVisibility = MutableStateFlow<Boolean>(false)
    val passwordVisibility: StateFlow<Boolean> = _passwordVisibility

    private val _buttonEnabled = MutableStateFlow<Boolean>(false)
    val buttonEnabled: StateFlow<Boolean> = _buttonEnabled

    private val _navigator = MutableLiveData<Navigator>(Navigator.Screen1)
    val navigator: LiveData<Navigator> = _navigator

    private val _alertDialogError = MutableStateFlow<Boolean>(false)
    val alertDialogError: StateFlow<Boolean> = _alertDialogError

    fun onPasswordVisibilityChanged(passwordVisibility: Boolean) {
        _passwordVisibility.value = !passwordVisibility
        _image.value = if (passwordVisibility) {
            Icons.Filled.Visibility
        } else {
            Icons.Filled.VisibilityOff
        }
    }

    fun changeEmPwLgS(email: String, password: String) {
        _email.value = email
        _password.value = password
        _buttonEnabled.value =
            Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 7
    }

    fun onLogin() {
        if (_email.value.isNotEmpty() && _password.value.isNotEmpty())
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _navigator.value = Navigator.Screen2
                        Log.d("LoginViewModel", "Cuenta logeada exitosamente.")
                    } else {
                        Log.e("LoginViewModel", "Error al logear la cuenta: ${it.exception?.message}")
                        _alertDialogError.value = true
                    }
                }
    }

    fun onCreateAccount(){
        if (_email.value.isNotEmpty() && _password.value.isNotEmpty())
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("LoginViewModel", "Cuenta creada exitosamente.")
                        _navigator.value = Navigator.Screen2
                    } else {
                        Log.e("LoginViewModel", "Error al crear cuenta: ${it.exception?.message}")
                        _alertDialogError.value = true
                    }
                }
    }

    fun unLog (){
        FirebaseAuth.getInstance().signOut()
        _navigator.value = Navigator.Screen1
        Log.d("LoginViewModel", "Cuenta cerrada exitosamente.")

    }

    fun confirmButton(){
        _email.value = ""
        _password.value = ""
        _alertDialogError.value = false
    }
}

