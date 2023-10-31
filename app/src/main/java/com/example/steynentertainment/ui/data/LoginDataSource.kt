package com.example.steynentertainment.ui.data

import com.example.steynentertainment.ui.data.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val isEmailVerified = firebaseUser?.isEmailVerified ?: false

                    // Check if firebaseUser is not null before proceeding
                    if (firebaseUser != null) {
                        val user = LoggedInUser(
                            firebaseUser = firebaseUser,
                            userId = firebaseUser.uid,
                            displayName = firebaseUser.email!!,
                            isEmailVerified = isEmailVerified
                        )
                        callback(Result.Success(user))
                    } else {
                        // Handle the case where firebaseUser is null
                        callback(Result.Error(IOException("Firebase user is null")))
                    }
                } else {
                    callback(Result.Error(IOException("Error logging in", task.exception)))
                }
            }
    }


    fun logout() {
        auth.signOut()
    }
}