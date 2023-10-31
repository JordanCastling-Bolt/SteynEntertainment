package com.example.steynentertainment.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.steynentertainment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    private val db = FirebaseFirestore.getInstance()

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> get() = _navigateToLogin

    fun register(email: String, firstName: String, lastName: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName("$firstName $lastName")
                    .build()

                user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification()

                        // Store user data in Firestore
                        val userData = hashMapOf(
                            "email" to email,
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "role" to "user",
                            "terms_accepted" to true,
                            "yearlyPayments" to 0,
                            "subscribed" to "no"
                        )
                        db.collection("Users")
                            .document(user.uid)
                            .set(userData)
                            .addOnSuccessListener {

                            }
                            .addOnFailureListener { e ->
                                // Handle failure
                                _registerResult.value = RegisterResult.Error(R.string.firestore_error)
                            }
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
    private fun sendEmailVerification() {
        val user = firebaseAuth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _registerResult.value = RegisterResult.Success(RegisteredUserView(displayName = "${user.displayName}"))
                _navigateToLogin.value = true // Trigger navigation
            } else {
                _registerResult.value = RegisterResult.Error(R.string.failed_to_send_verification_email)
            }
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
