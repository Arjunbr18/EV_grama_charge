package com.example.ev_grama_charge.data.model

data class Booking(
    val id: String = "",
    val hostName: String = "",
    val date: String = "",
    val time: String = "",
    val totalCost: Double = 0.0,
    var status: String = "",
    val timestamp: Long = 0L
)