package com.example.ev_grama_charge.models

data class HostModel(

    val hostId: String = "",

    val shopName: String = "",

    val ownerName: String = "",

    val phone: String = "",

    val address: String = "",

    val profileImage: String = "",

    val createdAt: Long = System.currentTimeMillis()
)