package com.example.steynentertainment.ui.ui.login

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.steynentertainment.R
import com.example.steynentertainment.ui.data.LoginRepository
import com.example.steynentertainment.ui.data.Result
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    // Initialize Firebase Analytics
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @RequiresApi(Build.VERSION_CODES.M)
    fun login(context: Context, username: String, password: String) {
        // Initialize Firebase Analytics with the context
        firebaseAnalytics = Firebase.analytics

        // Log login attempt with Firebase recommended method
        val bundleAttempt = Bundle()
        bundleAttempt.putString(FirebaseAnalytics.Param.METHOD, "Google")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundleAttempt)

        // Firebase authentication is asynchronous, so the result must be handled in a callback
        loginRepository.login(username, password) { result ->
            val bundle = Bundle()

            if (result is Result.Success) {
                val loggedInUser = result.data
                val firebaseUser = loggedInUser.firebaseUser  // Now you have the FirebaseUser


                _loginResult.value = LoginResult(success = LoggedInUserView(displayName = firebaseUser.displayName ?: ""))
                bundle.putString(FirebaseAnalytics.Param.METHOD, "Google")
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)

                if (!firebaseUser.isEmailVerified) {
                    firebaseUser.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _loginResult.value = LoginResult(error = R.string.email_not_verified_resend)
                            } else {
                                _loginResult.value = LoginResult(error = R.string.email_resend_failed)
                            }
                        }
                    bundle.putString(FirebaseAnalytics.Param.METHOD, "Google")
                    firebaseAnalytics.logEvent("login_fail_email_not_verified", bundle)
                }
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
                // Log login failure
                bundle.putString(FirebaseAnalytics.Param.METHOD, "Google")
                firebaseAnalytics.logEvent("login_fail", bundle)
            }
        }
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