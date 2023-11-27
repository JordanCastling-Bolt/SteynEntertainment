package com.example.steynentertainment.ui.register

// data class for register
data class RegisterFromState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val isDataValid: Boolean = false
)
