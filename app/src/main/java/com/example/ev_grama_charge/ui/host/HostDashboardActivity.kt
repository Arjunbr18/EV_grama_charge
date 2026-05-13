package com.example.ev_grama_charge.ui.host

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.R
import com.google.firebase.firestore.FirebaseFirestore

class HostDashboardActivity : AppCompatActivity() {

    private lateinit var tvTotalBookings: TextView
    private lateinit var tvTotalEarnings: TextView
    private lateinit var tvTodayBookings: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_dashboard)

        tvTotalBookings = findViewById(R.id.tvTotalBookings)
        tvTotalEarnings = findViewById(R.id.tvTotalEarnings)
        tvTodayBookings = findViewById(R.id.tvTodayBookings)

        loadDashboardData()
    }

    private fun loadDashboardData() {
        FirebaseFirestore.getInstance()
            .collection("bookings")
            .get()
            .addOnSuccessListener { result ->

                var totalBookings = 0
                var totalEarnings = 0.0
                var todayBookings = 0

                val today = java.text.SimpleDateFormat("d/M/yyyy").format(java.util.Date())

                for (doc in result) {

                    totalBookings++

                    val cost = when (val c = doc.get("totalCost")) {
                        is Double -> c
                        is Long -> c.toDouble()
                        is String -> c.replace("₹", "").toDoubleOrNull() ?: 0.0
                        else -> 0.0
                    }

                    totalEarnings += cost

                    val date = doc.getString("date") ?: ""
                    if (date == today) {
                        todayBookings++
                    }
                }

                tvTotalBookings.text = "Total Bookings: $totalBookings"
                tvTotalEarnings.text = "Total Earnings: ₹%.2f".format(totalEarnings)
                tvTodayBookings.text = "Today's Bookings: $todayBookings"
            }
    }
}