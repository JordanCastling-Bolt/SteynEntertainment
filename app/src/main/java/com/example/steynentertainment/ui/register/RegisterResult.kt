package com.example.steynentertainment.ui.register

/**
 * Authentication result : success (user details) or error message.
 */
sealed class RegisterResult {
    data class Success(val userView: RegisteredUserView) : RegisterResult()
    data class Error(val error: Int) : RegisterResult()
}

data class RegisteredUserView(val displayName: String)
