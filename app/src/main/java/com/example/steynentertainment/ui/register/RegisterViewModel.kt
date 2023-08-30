package com.example.steynentertainment.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.steynentertainment.R
import com.example.steynentertainment.ui.data.RegisterRepository
import com.example.steynentertainment.ui.data.Result
import com.example.steynentertainment.ui.register.RegisteredUserView

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<Any>()
    val registerResult: LiveData<Any> = _registerResult

    fun register(username: String, password: String, confirmPassword: String) {
        // Add your asynchronous job for registration here
        val result = registerRepository.register(username, password, confirmPassword)

        if (result is Result.Success) {  // Specify type
            _registerResult.value = RegisteredUserView(displayName = result.data.displayName)
        } else {
            _registerResult.value = RegisterResult(error = R.string.register_failed)
        }
    }

    fun registerDataChanged(username: String, password: String, confirmPassword: String) {
        if (!isUserNameValid(username)) {
            _registerForm.value = RegisterFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password)
        } else if (password != confirmPassword) {
            _registerForm.value = RegisterFormState(confirmPasswordError = R.string.password_mismatch)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
    }

    // Validation checks similar to the login page
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
