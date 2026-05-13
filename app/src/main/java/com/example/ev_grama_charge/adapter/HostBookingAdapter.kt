package com.example.ev_grama_charge.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ev_grama_charge.databinding.ItemHostBookingBinding
import com.example.ev_grama_charge.models.BookingModel
import com.example.ev_grama_charge.models.TransactionModel
import com.example.ev_grama_charge.ui.host.HostQrScannerActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class HostBookingAdapter(

    private val bookingList:
    ArrayList<BookingModel>

) : RecyclerView.Adapter<HostBookingAdapter.ViewHolder>() {

    inner class ViewHolder(

        val binding:
        ItemHostBookingBinding

    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding =
            ItemHostBookingBinding.inflate(

                LayoutInflater.from(
                    parent.context
                ),

                parent,
                false
            )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val booking =
            bookingList[position]

        holder.binding.tvUserName.text =
            booking.userName

        holder.binding.tvChargerName.text =
            booking.chargerName

        holder.binding.tvDateTime.text =
            "${booking.bookingDate} • ${booking.bookingTime}"

        holder.binding.tvDuration.text =
            booking.duration

        holder.binding.tvAmount.text =
            "₹${booking.amount}"

        holder.binding.tvStatus.text =
            booking.status

        // STATUS COLORS

        when (booking.status) {

            "PENDING" -> {

                holder.binding.tvStatus
                    .setTextColor(
                        Color.parseColor("#F59E0B")
                    )
            }

            "ACCEPTED" -> {

                holder.binding.tvStatus
                    .setTextColor(
                        Color.parseColor("#10B981")
                    )
            }

            "REJECTED" -> {

                holder.binding.tvStatus
                    .setTextColor(
                        Color.parseColor("#EF4444")
                    )
            }

            "COMPLETED" -> {

                holder.binding.tvStatus
                    .setTextColor(
                        Color.parseColor("#2563EB")
                    )
            }
        }

        // BUTTON VISIBILITY

        when (booking.status) {

            "PENDING" -> {

                holder.binding.btnAccept.visibility =
                    View.VISIBLE

                holder.binding.btnReject.visibility =
                    View.VISIBLE

                holder.binding.btnComplete.visibility =
                    View.GONE

                holder.binding.btnScanQr.visibility =
                    View.GONE
            }

            "ACCEPTED" -> {

                holder.binding.btnAccept.visibility =
                    View.GONE

                holder.binding.btnReject.visibility =
                    View.GONE

                holder.binding.btnComplete.visibility =
                    View.GONE

                holder.binding.btnScanQr.visibility =
                    View.VISIBLE
            }

            else -> {

                holder.binding.btnAccept.visibility =
                    View.GONE

                holder.binding.btnReject.visibility =
                    View.GONE

                holder.binding.btnComplete.visibility =
                    View.GONE

                holder.binding.btnScanQr.visibility =
                    View.GONE
            }
        }

        // ACCEPT BOOKING

        holder.binding.btnAccept
            .setOnClickListener {

                updateStatus(
                    booking.bookingId,
                    "ACCEPTED"
                )

                Toast.makeText(

                    holder.itemView.context,

                    "Booking Accepted",

                    Toast.LENGTH_SHORT

                ).show()
            }

        // REJECT BOOKING

        holder.binding.btnReject
            .setOnClickListener {

                updateStatus(
                    booking.bookingId,
                    "REJECTED"
                )

                Toast.makeText(

                    holder.itemView.context,

                    "Booking Rejected",

                    Toast.LENGTH_SHORT

                ).show()
            }

        // SCAN QR

        holder.binding.btnScanQr
            .setOnClickListener {

                val intent =
                    Intent(

                        holder.itemView.context,

                        HostQrScannerActivity::class.java
                    )

                holder.itemView.context
                    .startActivity(intent)
            }

        // COMPLETE MANUALLY

        holder.binding.btnComplete
            .setOnClickListener {

                completeBooking(
                    booking
                )

                Toast.makeText(

                    holder.itemView.context,

                    "Booking Completed",

                    Toast.LENGTH_SHORT

                ).show()
            }
    }

    private fun updateStatus(
        bookingId: String,
        status: String
    ) {

        FirebaseFirestore.getInstance()

            .collection("bookings")

            .document(bookingId)

            .update(
                "status",
                status
            )
    }

    private fun completeBooking(
        booking: BookingModel
    ) {

        val db =
            FirebaseFirestore.getInstance()

        // UPDATE BOOKING STATUS

        db.collection("bookings")
            .document(booking.bookingId)
            .update(
                "status",
                "COMPLETED"
            )

        val walletRef =
            db.collection("wallets")
                .document(booking.hostId)

        val amount =
            booking.amount
                .toDoubleOrNull()
                ?: 0.0

        // CREATE / UPDATE WALLET

        walletRef.get()

            .addOnSuccessListener { snapshot ->

                if (snapshot.exists()) {

                    val currentBalance =
                        snapshot.getDouble(
                            "walletBalance"
                        ) ?: 0.0

                    val totalEarnings =
                        snapshot.getDouble(
                            "totalEarnings"
                        ) ?: 0.0

                    val pendingPayout =
                        snapshot.getDouble(
                            "pendingPayout"
                        ) ?: 0.0

                    walletRef.update(

                        mapOf(

                            "walletBalance" to
                                    currentBalance + amount,

                            "totalEarnings" to
                                    totalEarnings + amount,

                            "pendingPayout" to
                                    pendingPayout + amount
                        )
                    )

                } else {

                    val walletData =
                        hashMapOf(

                            "walletBalance" to amount,

                            "totalEarnings" to amount,

                            "pendingPayout" to amount
                        )

                    walletRef.set(walletData)
                }
            }

        // CREATE TRANSACTION

        val transactionId =
            UUID.randomUUID().toString()

        val transaction =
            TransactionModel(

                transactionId =
                    transactionId,

                hostId =
                    booking.hostId,

                bookingId =
                    booking.bookingId,

                amount =
                    amount,

                type =
                    "Charging Payment",

                status =
                    "SUCCESS"
            )

        db.collection("transactions")
            .document(transactionId)
            .set(transaction)
    }

    override fun getItemCount():
            Int = bookingList.size
}