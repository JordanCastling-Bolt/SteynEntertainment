package com.example.steynentertainment.ui.data

import com.example.steynentertainment.ui.data.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val isEmailVerified = firebaseUser?.isEmailVerified ?: false

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        // Delegate the call to the data source
        dataSource.login(username, password, callback)
    }



    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}