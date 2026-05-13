package com.example.ev_grama_charge.ui.user

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.models.ChargerModel
import com.example.ev_grama_charge.ui.booking.BookingActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore

class HostDetailsActivity : AppCompatActivity() {

    private lateinit var recyclerChargers:
            RecyclerView

    private lateinit var chargerAdapter:
            ChargerSelectionAdapter

    private lateinit var btnBook:
            MaterialButton

    private val chargerList =
        ArrayList<ChargerModel>()

    private var selectedCharger:
            ChargerModel? = null

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_host_details
        )

        // GET DATA

        val hostId =
            intent.getStringExtra(
                "hostId"
            ) ?: ""

        val name =
            intent.getStringExtra(
                "name"
            ) ?: "Unknown"

        val price =
            intent.getDoubleExtra(
                "price",
                0.0
            )

        val rating =
            intent.getDoubleExtra(
                "rating",
                0.0
            )

        val distance =
            intent.getDoubleExtra(
                "distance",
                0.0
            )

        val available =
            intent.getBooleanExtra(
                "available",
                true
            )

        // BIND VIEWS

        val tvName =
            findViewById<TextView>(
                R.id.tvName
            )

        val tvPrice =
            findViewById<TextView>(
                R.id.tvPrice
            )

        val tvRating =
            findViewById<TextView>(
                R.id.tvRating
            )

        val tvDistance =
            findViewById<TextView>(
                R.id.tvDistance
            )

        val tvStatus =
            findViewById<TextView>(
                R.id.tvStatus
            )

        btnBook =
            findViewById(
                R.id.btnBook
            )

        recyclerChargers =
            findViewById(
                R.id.recyclerChargers
            )

        // SET VALUES

        tvName.text =
            name

        tvPrice.text =
            "Starting ₹$price/hr"

        tvRating.text =
            "⭐ $rating"

        tvDistance.text =
            "$distance km"

        tvStatus.text =
            if (available)
                "Available"
            else
                "Busy"

        // RECYCLER

        recyclerChargers.layoutManager =
            LinearLayoutManager(this)

        chargerAdapter =
            ChargerSelectionAdapter(

                chargerList

            ) { charger ->

                selectedCharger =
                    charger
            }

        recyclerChargers.adapter =
            chargerAdapter

        // BUTTON DEFAULT

        btnBook.isEnabled =
            false

        btnBook.text =
            "Loading Chargers..."

        // LOAD CHARGERS

        loadChargers(hostId)

        // BOOK CLICK

        btnBook.setOnClickListener {

            val charger =
                selectedCharger

            if (charger == null) {

                Toast.makeText(

                    this,

                    "Select charger first",

                    Toast.LENGTH_SHORT

                ).show()

                return@setOnClickListener
            }

            val bookingIntent =
                Intent(
                    this,
                    BookingActivity::class.java
                )

            bookingIntent.putExtra(
                "hostId",
                hostId
            )

            bookingIntent.putExtra(
                "chargerId",
                charger.chargerId
            )

            bookingIntent.putExtra(
                "chargerName",
                charger.chargerName
            )

            bookingIntent.putExtra(
                "name",
                name
            )

            bookingIntent.putExtra(
                "price",
                charger.pricePerHour
                    .toDoubleOrNull()
                    ?: 0.0
            )

            startActivity(
                bookingIntent
            )

            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }

    private fun loadChargers(
        hostId: String
    ) {

        FirebaseFirestore.getInstance()

            .collection("chargers")

            .whereEqualTo(
                "hostId",
                hostId
            )

            .addSnapshotListener { value, error ->

                if (
                    error != null ||
                    value == null
                ) {

                    btnBook.isEnabled =
                        false

                    btnBook.text =
                        "Failed To Load"

                    return@addSnapshotListener
                }

                chargerList.clear()

                for (document in value.documents) {

                    val charger =
                        document.toObject(
                            ChargerModel::class.java
                        )

                    charger?.let {

                        chargerList.add(it)
                    }
                }

                chargerAdapter.notifyDataSetChanged()

                // AUTO SELECT FIRST

                if (
                    chargerList.isNotEmpty()
                ) {

                    selectedCharger =
                        chargerList[0]

                    btnBook.isEnabled =
                        true

                    btnBook.text =
                        "Book Selected Charger"

                } else {

                    btnBook.isEnabled =
                        false

                    btnBook.text =
                        "No Chargers Available"
                }
            }
    }

    override fun finish() {

        super.finish()

        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }
}