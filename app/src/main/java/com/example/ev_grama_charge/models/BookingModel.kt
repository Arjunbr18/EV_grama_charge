package com.example.ev_grama_charge.models

data class BookingModel(

    val bookingId: String = "",

    val userId: String = "",

    val hostId: String = "",

    val chargerId: String = "",

    val chargerName: String = "",

    val userName: String = "",

    val userPhone: String = "",

    val bookingDate: String = "",

    val bookingTime: String = "",

    val duration: String = "",

    val amount: String = "",

    val status: String = "PENDING",

    val timestamp: Long = System.currentTimeMillis()
)