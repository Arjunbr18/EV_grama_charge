package com.example.ev_grama_charge.models

data class AvailabilityModel(

    val hostId: String = "",

    val openingTime: String = "",

    val closingTime: String = "",

    val maintenanceMode: Boolean = false,

    val workingDays:
    List<String> = emptyList(),

    val blockedDates:
    List<String> = emptyList()
)