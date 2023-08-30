package com.example.steynentertainment.ui.register

/**
 * Authentication result : success (user details) or error message.
 */
data class RegisterResult(
    val success: RegisteredUserView? = null,
    val error: Int? = null
)

/**
 * User details post-registration that is exposed to the UI
 */
data class RegisteredUserView(
    val displayName: String
    // You can add more fields here that you may want to show in your UI
)