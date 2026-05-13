package com.example.ev_grama_charge.ui.host

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.databinding.ActivityAddChargerBinding
import com.example.ev_grama_charge.models.ChargerModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class AddChargerActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityAddChargerBinding

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityAddChargerBinding.inflate(
                layoutInflater
            )

        setContentView(
            binding.root
        )

        setupClickListeners()
    }

    private fun setupClickListeners() {

        binding.btnSaveCharger
            .setOnClickListener {

                saveCharger()
            }
    }

    private fun saveCharger() {

        val chargerName =
            binding.etChargerName
                .text.toString().trim()

        val chargerType =
            binding.etChargerType
                .text.toString().trim()

        val connectorType =
            binding.etConnectorType
                .text.toString().trim()

        val chargingSpeed =
            binding.etChargingSpeed
                .text.toString().trim()

        val price =
            binding.etPrice
                .text.toString().trim()

        if (

            chargerName.isEmpty() ||

            chargerType.isEmpty() ||

            connectorType.isEmpty() ||

            chargingSpeed.isEmpty() ||

            price.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Fill all fields",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val chargerId =
            UUID.randomUUID()
                .toString()

        val hostId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        val charger = ChargerModel(

            chargerId = chargerId,

            hostId = hostId,

            chargerName = chargerName,

            chargerType = chargerType,

            connectorType = connectorType,

            chargingSpeed = chargingSpeed,

            pricePerHour = price,

            available = true
        )

        FirebaseFirestore.getInstance()
            .collection("chargers")
            .document(chargerId)
            .set(charger)

            .addOnSuccessListener {

                // UPDATE HOST PRICE

                FirebaseFirestore.getInstance()
                    .collection("hosts")
                    .document(hostId)
                    .update(
                        "price",
                        price.toDouble()
                    )

                Toast.makeText(
                    this,
                    "Charger Added",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }

            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed To Add Charger",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}