package com.example.ev_grama_charge.ui.host

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.databinding.ActivityEditChargerBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditChargerActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityEditChargerBinding

    private lateinit var chargerId: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityEditChargerBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        chargerId =
            intent.getStringExtra("chargerId")
                ?: ""

        loadData()

        setupClickListeners()
    }

    private fun loadData() {

        binding.etChargerName.setText(
            intent.getStringExtra("chargerName")
        )

        binding.etChargerType.setText(
            intent.getStringExtra("chargerType")
        )

        binding.etConnectorType.setText(
            intent.getStringExtra("connectorType")
        )

        binding.etChargingSpeed.setText(
            intent.getStringExtra("chargingSpeed")
        )

        binding.etPrice.setText(
            intent.getStringExtra("price")
        )
    }

    private fun setupClickListeners() {

        binding.btnUpdateCharger
            .setOnClickListener {

                updateCharger()
            }
    }

    private fun updateCharger() {

        val updates = hashMapOf<String, Any>(

            "chargerName" to
                    binding.etChargerName.text.toString(),

            "chargerType" to
                    binding.etChargerType.text.toString(),

            "connectorType" to
                    binding.etConnectorType.text.toString(),

            "chargingSpeed" to
                    binding.etChargingSpeed.text.toString(),

            "pricePerHour" to
                    binding.etPrice.text.toString()
        )

        FirebaseFirestore.getInstance()
            .collection("chargers")
            .document(chargerId)
            .update(updates)
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Charger Updated",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Update Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}