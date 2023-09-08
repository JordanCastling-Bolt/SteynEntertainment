package com.example.steynentertainment.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.steynentertainment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterViewModel(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> get() = _navigateToLogin

    fun register(email: String, firstName: String, lastName: String, password: String) {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName("$firstName $lastName")
                    // .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg")) // Optional, if you want to set a profile picture
                    .build()

                user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _registerResult.value = RegisterResult.Success(RegisteredUserView(displayName = "$firstName $lastName"))
                        _navigateToLogin.value = true // Trigger navigation
                    } else {
                        _registerResult.value = RegisterResult.Error(R.string.failed_to_update_profile)
                    }
                }
            } else {
                _registerResult.value = RegisterResult.Error(R.string.register_failed)
            }
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
