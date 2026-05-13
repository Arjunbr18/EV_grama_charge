package com.example.ev_grama_charge.data.model

data class Host(

    var hostId: String = "",

    var name: String = "",

    var latitude: Double = 0.0,

    var longitude: Double = 0.0,

    var price: Double = 120.0,

    var rating: Double = 4.5,

    var distance: Double = 0.0,

    var isAvailable: Boolean = true
)