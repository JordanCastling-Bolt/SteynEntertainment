package com.example.steynentertainment.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.steynentertainment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

// RegisterViewModel is a ViewModel class designed for managing the registration logic and UI state in the RegisterActivity.
class RegisterViewModel(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    // LiveData for holding and observing the state of the registration form.
    private val _registerForm = MutableLiveData<RegisterFromState>()
    val registerFromState: LiveData<RegisterFromState> = _registerForm

    // LiveData for holding and observing the result of the registration process.
    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    // Firebase Firestore instance for storing user data.
    private val db = FirebaseFirestore.getInstance()

    // LiveData for navigating to the login screen after successful registration.
    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> get() = _navigateToLogin

    // register function handles the user registration process using Firebase Authentication.
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
                            "subscribed" to "no",
                            "profilePicture" to "https://firebasestorage.googleapis.com/v0/b/steynentertainment-800ea.appspot.com/o/user%2Fimage%2FDefault_Profile_Picture.jpeg?alt=media&token=6fbd2675-bb34-45a3-8912-c663320d85ea"
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

    // registerDataChanged function validates the registration form data and updates the form state.
    fun registerDataChanged(username: String, password: String, confirmPassword: String) {
        if (!isUserNameValid(username)) {
            _registerForm.value = RegisterFromState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = RegisterFromState(passwordError = R.string.invalid_password)
        } else if (password != confirmPassword) {
            _registerForm.value = RegisterFromState(confirmPasswordError = R.string.password_mismatch)
        } else {
            _registerForm.value = RegisterFromState(isDataValid = true)
        }
    }

    // sendEmailVerification function sends an email verification to the newly registered user.
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

    // Helper functions for validating username (email) and password.
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
