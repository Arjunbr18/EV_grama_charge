package com.example.ev_grama_charge.ui.user

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ev_grama_charge.adapter.UserBookingAdapter
import com.example.ev_grama_charge.databinding.ActivityUserBookingsBinding
import com.example.ev_grama_charge.models.BookingModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserBookingsActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityUserBookingsBinding

    private lateinit var adapter:
            UserBookingAdapter

    private val bookingList =
        ArrayList<BookingModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityUserBookingsBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        setupRecyclerView()

        loadBookings()
    }

    private fun setupRecyclerView() {

        adapter =
            UserBookingAdapter(bookingList)

        binding.recyclerUserBookings.apply {

            layoutManager =
                LinearLayoutManager(
                    this@UserBookingsActivity
                )

            adapter =
                this@UserBookingsActivity.adapter
        }
    }

    private fun loadBookings() {

        val userId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("bookings")
            .whereEqualTo("userId", userId)
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

                    var activeBookings = 0

                    for (document in value.documents) {

                        val booking =
                            document.toObject(
                                BookingModel::class.java
                            )

                        booking?.let {

                            bookingList.add(it)

                            if (
                                it.status == "PENDING" ||
                                it.status == "ACCEPTED"
                            ) {

                                activeBookings++
                            }
                        }
                    }

                    binding.tvTotalBookings.text =
                        bookingList.size.toString()

                    binding.tvActiveBookings.text =
                        activeBookings.toString()
                }

                adapter.notifyDataSetChanged()
            }
    }
}