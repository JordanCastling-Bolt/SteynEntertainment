package com.example.steynentertainment.ui

//This is the data class for User's within the Steyn Entertainment Ecosystem, with the relevant attributes
data class Users(
    val email: String?="",
    val firstName: String?= "",
    val lastName: String?= "",
    val role: String?="",
    val subscribed: String?="",
    val yearlyPayments: Int?=null
)


