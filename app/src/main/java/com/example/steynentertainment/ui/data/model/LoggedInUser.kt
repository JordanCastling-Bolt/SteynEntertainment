package com.example.steynentertainment.ui.data.model

import com.google.firebase.auth.FirebaseUser

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val firebaseUser: FirebaseUser,
    val userId: String,
    val displayName: String,
    val isEmailVerified: Boolean
)
