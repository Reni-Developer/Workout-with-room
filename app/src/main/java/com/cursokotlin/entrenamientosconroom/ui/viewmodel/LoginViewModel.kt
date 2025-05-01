package com.cursokotlin.entrenamientosconroom.ui.viewmodel

import android.app.Activity
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
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
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
    private val credentialManager: CredentialManager
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

    private val _stateFirstSignIn = MutableStateFlow<Boolean>(true)
    val stateFirstSignIn: StateFlow<Boolean> = _stateFirstSignIn

    private val _navigator = MutableLiveData<Navigator>(Navigator.Screen1)
    val navigator: LiveData<Navigator> = _navigator

    private val _errorType = MutableStateFlow<Int>(99)
    val errorType: StateFlow<Int> = _errorType

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
                        signIn()
                        Log.d("LoginViewModel", "Cuenta logeada exitosamente.")
                    } else {
                        Log.e(
                            "LoginViewModel",
                            "Error al logear la cuenta: ${it.exception?.message}"
                        )
                        createError(0)
                    }
                }
    }

    fun onCreateAccount() {
        if (_email.value.isNotEmpty() && _password.value.isNotEmpty())
            auth
                .createUserWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        signIn()
                        Log.d("LoginViewModel", "Cuenta creada exitosamente.")
                    } else {
                        Log.e("LoginViewModel", "Error al crear cuenta: ${it.exception?.message}")
                        createError(1)
                    }
                }
    }

    fun firstSignIn(activity: Activity) {
        //Solicitud de inicio de seción de Google con GetGoogleIdOption
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(activity.getString(R.string.default_web_client_id))
            .setNonce("")
            .build()
        //Solicitud de credenciales
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        //Paso de solicitud para obtener credenciales, para recuperar las disponibles del user
        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(activity, request)
                val rawCredential = result.credential
                if (rawCredential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val credential =
                        GoogleIdTokenCredential.createFrom(rawCredential.data)
                    val idToken = credential.idToken
                    if (idToken.isNotEmpty()) {
                        firebaseAuthWithGoogle(idToken)
                    } else { Log.e("LoginViewModel", "Token vacío") }
                } else { Log.e("LoginViewModel", "No coincide la credencial") }
            } catch (e: Exception) {
                _stateFirstSignIn.value = false
                Log.e("LoginViewModel", "Error en googleSignIn: ${e.message}")
            }
        }
    }

    fun signInWithGoogleButton(activity: Activity) {
        //Solicitud de inicio de seción de Google con GetSignInWithGoogleOption
        val signInWithGoogleOption = GetSignInWithGoogleOption
            .Builder(serverClientId = activity.getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(activity, request)
                val rawCredential = result.credential
                if (rawCredential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val credential =
                        GoogleIdTokenCredential.createFrom(rawCredential.data)
                    val idToken = credential.idToken
                    if (idToken.isNotEmpty()) {
                        firebaseAuthWithGoogle(idToken)
                    } else { Log.e("LoginViewModel", "Token vacío") }
                } else { Log.e("LoginViewModel", "No coincide la credencial") }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error en googleSignIn: ${e.message}")
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Log.d("LoginViewModel", "$credential")
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("LoginViewModel", "signInWithCredential:success")
                    signIn()
                } else {
                    Log.w("LoginViewModel", "signInWithCredential:failure", it.exception)
                    createError(3)
                }
            }
    }

    fun signIn() {
        _stateFirstSignIn.value = false
        _navigator.value = Navigator.Screen2
    }

    fun signOut() {
        _stateFirstSignIn.value = true
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
        _errorType.value = 99
    }

    fun createError(errorType: Int) {
        _errorType.value = errorType
    }

}

