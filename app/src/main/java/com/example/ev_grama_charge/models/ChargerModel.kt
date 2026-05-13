package com.example.ev_grama_charge.models

data class ChargerModel(

    val chargerId: String = "",

    val hostId: String = "",

    val chargerName: String = "",

    val chargerType: String = "",

    val connectorType: String = "",

    val chargingSpeed: String = "",

    val pricePerHour: String = "",

    val available: Boolean = true
)