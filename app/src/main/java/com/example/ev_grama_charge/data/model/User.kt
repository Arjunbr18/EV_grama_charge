package com.example.ev_grama_charge.data.model

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val fcmToken: String = ""
)