package com.example.ev_grama_charge.ui.host

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ev_grama_charge.adapter.HostBookingAdapter
import com.example.ev_grama_charge.databinding.ActivityHostBookingsBinding
import com.example.ev_grama_charge.models.BookingModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HostBookingsActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityHostBookingsBinding

    private lateinit var adapter:
            HostBookingAdapter

    private val bookingList =
        ArrayList<BookingModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityHostBookingsBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        setupRecyclerView()

        loadBookings()
    }

    private fun setupRecyclerView() {

        adapter =
            HostBookingAdapter(bookingList)

        binding.recyclerBookings.apply {

            layoutManager =
                LinearLayoutManager(
                    this@HostBookingsActivity
                )

            adapter = this@HostBookingsActivity.adapter
        }
    }

    private fun loadBookings() {

        val hostId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("bookings")
            .whereEqualTo("hostId", hostId)
            .addSnapshotListener { value, error ->

                if (error != null) {

                    return@addSnapshotListener
                }

                bookingList.clear()

                if (
                    value == null ||
                    value.isEmpty
                ) {

                    binding.tvEmptyBookings.visibility =
                        View.VISIBLE

                } else {

                    binding.tvEmptyBookings.visibility =
                        View.GONE

                    for (document in value.documents) {

                        val booking =
                            document.toObject(
                                BookingModel::class.java
                            )

                        booking?.let {

                            bookingList.add(it)
                        }
                    }
                }

                adapter.notifyDataSetChanged()
            }
    }
}