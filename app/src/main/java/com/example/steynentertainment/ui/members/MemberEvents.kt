package com.example.steynentertainment.ui.members

// MemberEvents is a data class representing the details of events specifically for members.
// This class is designed to encapsulate the properties of an event in a concise and immutable way.
data class MemberEvents(
    val category: String? = "",
    val date: String? = "",
    val description: String? = "",
    val picture: String? = "",
    val title: String? = "",
    val url: String? = ""
)
