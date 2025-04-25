package com.cursokotlin.entrenamientosconroom.ui.viewmodel

import android.media.Image
import android.util.Patterns
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _image = MutableStateFlow<ImageVector>(Icons.Filled.Visibility)
    val image: StateFlow<ImageVector> = _image

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _passwordVisibility = MutableStateFlow<Boolean>(false)
    val passwordVisibility: StateFlow<Boolean> = _passwordVisibility

    private val _buttonEnabled = MutableStateFlow<Boolean>(false)
    val buttonEnabled: StateFlow<Boolean> = _buttonEnabled

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
        _buttonEnabled.value = Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 7
    }

}