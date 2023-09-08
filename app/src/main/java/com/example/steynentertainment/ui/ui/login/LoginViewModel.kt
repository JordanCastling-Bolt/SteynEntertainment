package com.example.steynentertainment.ui.ui.login

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.steynentertainment.R
import com.example.steynentertainment.ui.data.KeystoreHelper
import com.example.steynentertainment.ui.data.LoginRepository
import com.example.steynentertainment.ui.data.Result
import com.example.steynentertainment.ui.data.SharedPreferencesHelper
import com.example.steynentertainment.ui.data.model.LoggedInUser
import java.security.SecureRandom


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    @RequiresApi(Build.VERSION_CODES.M)
    fun login(context: Context, username: String, password: String) {
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.displayName))

            // Generate your session token here
            val sessionToken = generateSessionToken(result.data) // Implement your token generation logic
            // Log to check if the session token is being generated
            Log.d("TokenCheck", "Session token is generated: $sessionToken")
            // Encrypt the token using Keystore
            KeystoreHelper.generateSecretKey()
            val encryptedToken = KeystoreHelper.encrypt(sessionToken)

            // Save this encrypted token to SharedPreferences
            SharedPreferencesHelper.saveToken(context, encryptedToken)


        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }


    // A dummy function to generate a session token (You should replace this with a real implementation)
    private fun generateSessionToken(data: LoggedInUser): String {
        // Generate a random 64-byte (512-bit) value
        val random = SecureRandom()
        val bytes = ByteArray(64)
        random.nextBytes(bytes)

        // Convert the random bytes to a hexadecimal string
        val token = bytes.joinToString("") { "%02x".format(it) }

        // Add some user-specific information (e.g., user ID) and timestamp to the token
        val timestamp = System.currentTimeMillis()
        Log.d("TokenGeneration", "Generated Token: $token")
        return "$token-UID${data.userId}-TS$timestamp"
    }


    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}