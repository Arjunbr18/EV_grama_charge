package com.example.ev_grama_charge.models

data class ReviewModel(

    val reviewId: String = "",

    val hostId: String = "",

    val userId: String = "",

    val userName: String = "",

    val rating: Float = 0f,

    val review: String = "",

    val timestamp: Long =
        System.currentTimeMillis()
)