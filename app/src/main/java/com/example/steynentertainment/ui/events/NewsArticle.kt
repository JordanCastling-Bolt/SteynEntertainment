package com.example.steynentertainment.ui.events

// NewsArticle is a data class representing the details of a news article.
// As a data class in Kotlin, it primarily serves to hold data and automatically provides functionality like equals, hashCode, and toString.
data class NewsArticle(
    val category: String,
    val content: String,
    val imageUrl: String,
    val timestamp: String,
    val title: String
)
