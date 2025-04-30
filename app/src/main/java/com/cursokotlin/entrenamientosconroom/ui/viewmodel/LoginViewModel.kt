package com.cursokotlin.entrenamientosconroom.ui.viewmodel

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.entrenamientosconroom.R
import com.cursokotlin.entrenamientosconroom.ui.Navigator
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val  credentialManager: CredentialManager
) : ViewModel() {

    private val _image = MutableStateFlow<ImageVector>(Icons.Filled.Visibility)
    val image: StateFlow<ImageVector> = _image

    private val _password = MutableStateFlow<String>("*Reni1234")
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

    private val _alertDialogErrorG = MutableStateFlow<Boolean>(false)
    val alertDialogErrorG: StateFlow<Boolean> = _alertDialogErrorG

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

    fun onEmailAndPassSignIn() {
        if (_email.value.isNotEmpty() && _password.value.isNotEmpty())
            auth
                .signInWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onLog()
                        Log.d("LoginViewModel", "Cuenta logeada exitosamente.")
                    } else {
                        Log.e(
                            "LoginViewModel",
                            "Error al logear la cuenta: ${it.exception?.message}"
                        )
                        createError()
                    }
                }
    }

    fun onCreateAccount() {
        if (_email.value.isNotEmpty() && _password.value.isNotEmpty())
            auth
                .createUserWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onLog()
                        Log.d("LoginViewModel", "Cuenta creada exitosamente.")
                    } else {
                        Log.e("LoginViewModel", "Error al crear cuenta: ${it.exception?.message}")
                        createError()
                    }
                }
    }

    fun onGoogleSignIn(activity: Context) {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(activity.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(activity, request)
                val rawCredential = result.credential
                if (rawCredential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val credential = GoogleIdTokenCredential.createFrom(rawCredential.data)
                    val idToken = credential.idToken
                    if (idToken.isNotEmpty()) {
                        firebaseAuthWithGoogle(idToken)
                    } else {
                        Log.e("LoginViewModel", "ID Token está vacío o nulo")
                    }
                } else {
                    Log.e("LoginViewModel", "ID Token es nulo")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error en googleSignIn: ${e.message}")
                createErrorG()
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("LoginViewModel", "signInWithCredential:success")
                    onLog()
                } else {
                    Log.w("LoginViewModel", "signInWithCredential:failure", it.exception)
                    createErrorG()
                }
            }
    }

    fun onLog() {
        _navigator.value = Navigator.Screen2
    }

    fun unLog() {
        auth.signOut()
        viewModelScope.launch {
            try {
                val clearRequest = ClearCredentialStateRequest()
                credentialManager.clearCredentialState(clearRequest)
                Log.d("LoginViewModel", "Cuenta Google cerrada exitosamente.")
            } catch (e: ClearCredentialException) {
                Log.e("LoginViewModel", "Couldn't clear user credentials: ${e.localizedMessage}")
            }
        }

        _navigator.value = Navigator.Screen1
        Log.d("LoginViewModel", "Cuenta cerrada exitosamente.")
    }

    fun confirmButton() {
        _alertDialogError.value = false
        _alertDialogErrorG.value = false
    }

    fun createError() {
        _alertDialogError.value = true
    }

    fun createErrorG() {
        _alertDialogErrorG.value = true
    }

}

