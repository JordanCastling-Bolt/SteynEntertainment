package com.example.steynentertainment.ui.events

// EventDetails is a data class representing the details of an event.
// As a data class in Kotlin, it primarily serves to hold data and automatically provides functionality like equals, hashCode, and toString.
data class EventDetails(
    val title: String,
    val date: String,
    val description: String,
    val picture: String,
    val url: String,
    val ticketUrl: String
)
