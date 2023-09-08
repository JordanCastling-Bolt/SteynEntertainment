package com.example.steynentertainment.ui.data

import com.example.steynentertainment.ui.data.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(username: String, password: String): Result<LoggedInUser> {
        var result: Result<LoggedInUser>? = null

        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val user = LoggedInUser(firebaseUser!!.uid, firebaseUser.email!!)
                    result = Result.Success(user)
                } else {
                    result = Result.Error(IOException("Error logging in", task.exception))
                }
            }
        return result ?: Result.Error(IOException("Unknown error occurred"))
    }

    fun logout() {
        auth.signOut()
    }
}