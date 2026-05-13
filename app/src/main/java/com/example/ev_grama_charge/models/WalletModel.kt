package com.example.ev_grama_charge.models

data class WalletModel(

    val hostId: String = "",

    val walletBalance: Double = 0.0,

    val totalEarnings: Double = 0.0,

    val pendingPayout: Double = 0.0,

    val totalWithdrawn: Double = 0.0
)