package com.example.ev_grama_charge.ui.booking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.data.model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookingHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookingAdapter

    private val bookingList =
        mutableListOf<Booking>()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_booking_history
        )

        recyclerView =
            findViewById(R.id.recyclerBookings)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        adapter =
            BookingAdapter(bookingList) { booking ->

                openReceipt(booking)
            }

        recyclerView.adapter =
            adapter

        loadBookings()
    }

    private fun loadBookings() {

        val userId =
            FirebaseAuth.getInstance()
                .currentUser?.uid

        if (userId == null) {

            Toast.makeText(
                this,
                "User not logged in",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        FirebaseFirestore.getInstance()

            .collection("bookings")

            .whereEqualTo(
                "userId",
                userId
            )

            .get()

            .addOnSuccessListener { result ->

                bookingList.clear()

                for (doc in result) {

                    val amount =
                        doc.getString("amount")
                            ?.toDoubleOrNull()
                            ?: 0.0

                    val timestamp =
                        (doc.get("timestamp")
                                as? Number)
                            ?.toLong()
                            ?: 0L

                    val booking =
                        Booking(

                            id = doc.id,

                            hostName =
                                doc.getString(
                                    "chargerName"
                                ) ?: "Unknown",

                            date =
                                doc.getString(
                                    "bookingDate"
                                ) ?: "",

                            time =
                                doc.getString(
                                    "bookingTime"
                                ) ?: "",

                            totalCost =
                                amount,

                            status =
                                doc.getString(
                                    "status"
                                ) ?: "PENDING",

                            timestamp =
                                timestamp
                        )

                    bookingList.add(booking)
                }

                bookingList.sortByDescending {
                    it.timestamp
                }

                adapter.notifyDataSetChanged()
            }

            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed to load bookings",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun openReceipt(
        booking: Booking
    ) {

        val intent =
            Intent(
                this,
                ReceiptActivity::class.java
            )

        intent.putExtra(
            "host",
            booking.hostName
        )

        intent.putExtra(
            "date",
            booking.date
        )

        intent.putExtra(
            "time",
            booking.time
        )

        intent.putExtra(
            "amount",
            booking.totalCost
        )

        startActivity(intent)
    }
}