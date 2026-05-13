package com.example.ev_grama_charge.models

data class TransactionModel(

    val transactionId: String = "",

    val hostId: String = "",

    val bookingId: String = "",

    val amount: Double = 0.0,

    val type: String = "",

    val status: String = "",

    val timestamp: Long =
        System.currentTimeMillis()
)