package com.example.ev_grama_charge.ui.host

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.databinding.ActivityHostQrScannerBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class HostQrScannerActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityHostQrScannerBinding

    private val barcodeLauncher =
        registerForActivityResult(
            ScanContract()
        ) { result ->

            if (
                result.contents != null
            ) {

                val bookingId =
                    result.contents

                binding.tvScanResult.text =
                    "Booking Verified\n$bookingId"

                verifyBooking(
                    bookingId
                )

            } else {

                Toast.makeText(
                    this,
                    "Scan Cancelled",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityHostQrScannerBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        binding.btnScanQr
            .setOnClickListener {

                startScanner()
            }
    }

    private fun startScanner() {

        val options =
            ScanOptions()

        options.setPrompt(
            "Scan Booking QR"
        )

        options.setBeepEnabled(true)

        options.setOrientationLocked(true)

        barcodeLauncher.launch(options)
    }

    private fun verifyBooking(
        bookingId: String
    ) {

        FirebaseFirestore.getInstance()
            .collection("bookings")
            .document(bookingId)

            .update(
                "status",
                "COMPLETED"
            )

            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Charging Session Completed",
                    Toast.LENGTH_LONG
                ).show()
            }

            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Invalid Booking",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}