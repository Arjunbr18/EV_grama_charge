package com.example.ev_grama_charge.ui.booking

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.data.model.Booking
import com.google.firebase.firestore.FirebaseFirestore

class BookingAdapter(

    private val list:
    MutableList<Booking>,

    private val onClick:
        (Booking) -> Unit

) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        val host:
                TextView =
            view.findViewById(R.id.tvHost)

        val dateTime:
                TextView =
            view.findViewById(R.id.tvDateTime)

        val cost:
                TextView =
            view.findViewById(R.id.tvCost)

        val status:
                TextView =
            view.findViewById(R.id.tvStatus)

        val cancel:
                Button =
            view.findViewById(R.id.btnCancel)

        val showQr:
                Button =
            view.findViewById(R.id.btnShowQr)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.item_booking,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val booking =
            list[position]

        val context =
            holder.itemView.context

        // HOST NAME

        holder.host.text =
            booking.hostName

        // DATE + TIME

        holder.dateTime.text =
            context.getString(
                R.string.booking_datetime,
                booking.date,
                booking.time
            )

        // COST

        holder.cost.text =
            context.getString(
                R.string.booking_cost,
                booking.totalCost
            )

        // STATUS TEXT

        holder.status.text =
            when (booking.status) {

                "ACCEPTED" ->
                    "ACCEPTED"

                "COMPLETED" ->
                    "COMPLETED"

                "CANCELLED" ->
                    "CANCELLED"

                else ->
                    "PENDING"
            }

        // STATUS BACKGROUND

        when (booking.status) {

            "ACCEPTED" ->

                holder.status
                    .setBackgroundResource(
                        R.drawable.bg_status_confirmed
                    )

            "COMPLETED" ->

                holder.status
                    .setBackgroundResource(
                        R.drawable.bg_card_gradient
                    )

            "CANCELLED" ->

                holder.status
                    .setBackgroundResource(
                        R.drawable.bg_status_cancelled
                    )

            else ->

                holder.status
                    .setBackgroundResource(
                        R.drawable.bg_status_pending
                    )
        }

        // CANCEL BUTTON

        holder.cancel.visibility =

            if (
                booking.status == "PENDING"
            )

                View.VISIBLE

            else

                View.GONE

        // SHOW QR BUTTON

        holder.showQr.visibility =

            if (
                booking.status == "ACCEPTED"
            )

                View.VISIBLE

            else

                View.GONE

        // QR CLICK

        holder.showQr.setOnClickListener {

            val intent =
                Intent(
                    context,
                    BookingQrActivity::class.java
                )

            intent.putExtra(
                "bookingId",
                booking.id
            )

            context.startActivity(intent)
        }

        // CANCEL CLICK

        holder.cancel.setOnClickListener {

            FirebaseFirestore.getInstance()

                .collection("bookings")

                .document(booking.id)

                .update(
                    "status",
                    "CANCELLED"
                )

                .addOnSuccessListener {

                    booking.status =
                        "CANCELLED"

                    notifyItemChanged(
                        position
                    )
                }

                .addOnFailureListener {

                    Toast.makeText(

                        context,

                        "Cancel failed",

                        Toast.LENGTH_SHORT

                    ).show()
                }
        }

        // OPEN RECEIPT

        holder.itemView
            .setOnClickListener {

                onClick(booking)
            }
    }

    override fun getItemCount():
            Int = list.size
}