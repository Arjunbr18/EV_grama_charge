package com.example.ev_grama_charge.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserHomeActivity : AppCompatActivity() {

    private lateinit var tvUserName:
            TextView

    private lateinit var tvBookings:
            TextView

    private lateinit var tvSaved:
            TextView

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_user_home
        )

        // INIT VIEWS

        tvUserName =
            findViewById(R.id.tvUserName)

        tvBookings =
            findViewById(R.id.tvBookings)

        tvSaved =
            findViewById(R.id.tvSaved)

        // LOAD USER DATA

        loadUserData()

        // PROFILE CLICK

        findViewById<View>(
            R.id.layoutProfile
        ).setOnClickListener {

            startActivity(

                Intent(
                    this,
                    UserProfileActivity::class.java
                )
            )

            overridePendingTransition(

                R.anim.slide_in_right,

                R.anim.slide_out_left
            )
        }

        // QUICK ACTIONS

        setupClick(
            R.id.cardMap,
            MapActivity::class.java
        )

        setupClick(
            R.id.cardHosts,
            HostListActivity::class.java
        )

        // UPDATED BOOKINGS SCREEN

        setupClick(
            R.id.cardBookings,
            UserBookingsActivity::class.java
        )

        setupClick(
            R.id.cardCalculator,
            CalculatorActivity::class.java
        )
    }

    private fun loadUserData() {

        val userId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()

            .collection("users")

            .document(userId)

            .get()

            .addOnSuccessListener { document ->

                val name =
                    document.getString("name")
                        ?: "User"

                tvUserName.text =
                    name
            }

        // LOAD BOOKINGS COUNT

        FirebaseFirestore.getInstance()

            .collection("bookings")

            .whereEqualTo(
                "userId",
                userId
            )

            .get()

            .addOnSuccessListener { result ->

                val totalBookings =
                    result.size()

                tvBookings.text =
                    totalBookings.toString()

                // SIMPLE CALCULATED SAVED %

                val saved =
                    if (totalBookings == 0) {
                        "0%"
                    } else {
                        "${totalBookings * 7}%"
                    }

                tvSaved.text =
                    saved
            }
    }

    // REUSABLE NAVIGATION

    private fun setupClick(
        viewId: Int,
        activityClass: Class<*>
    ) {

        findViewById<View>(viewId)

            .setOnClickListener {

                try {

                    val intent =
                        Intent(
                            this,
                            activityClass
                        )

                    startActivity(intent)

                    overridePendingTransition(

                        R.anim.slide_in_right,

                        R.anim.slide_out_left
                    )

                } catch (e: Exception) {

                    Toast.makeText(

                        this,

                        "Feature not available",

                        Toast.LENGTH_SHORT

                    ).show()
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