package com.example.ev_grama_charge.ui.host

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.data.model.Booking
import com.google.firebase.firestore.FirebaseFirestore

class HostBookingAdapter(private val bookings: MutableList<Booking>) :
    RecyclerView.Adapter<HostBookingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvHost: TextView = view.findViewById(R.id.tvHost)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvCost: TextView = view.findViewById(R.id.tvCost)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val btnAccept: Button = view.findViewById(R.id.btnAccept)
        val btnReject: Button = view.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_host_booking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val booking = bookings[position]

        val context = holder.itemView.context

        holder.tvHost.text = booking.hostName.ifEmpty {
            context.getString(R.string.unknown_host)
        }

        holder.tvDate.text = context.getString(
            R.string.booking_date,
            booking.date,
            booking.time
        )

        holder.tvCost.text = context.getString(
            R.string.booking_cost,
            booking.totalCost
        )

        holder.tvStatus.text = booking.status

        if (booking.status != "PENDING") {
            holder.btnAccept.visibility = View.GONE
            holder.btnReject.visibility = View.GONE
        } else {
            holder.btnAccept.visibility = View.VISIBLE
            holder.btnReject.visibility = View.VISIBLE
        }

        holder.btnAccept.setOnClickListener {
            updateStatus(booking.id, "CONFIRMED", holder)
        }

        holder.btnReject.setOnClickListener {
            updateStatus(booking.id, "CANCELLED", holder)
        }
    }

    private fun updateStatus(id: String, status: String, holder: ViewHolder) {
        FirebaseFirestore.getInstance()
            .collection("bookings")
            .document(id)
            .update("status", status)
            .addOnSuccessListener {
                holder.tvStatus.text = status
                holder.btnAccept.visibility = View.GONE
                holder.btnReject.visibility = View.GONE
            }
    }

    override fun getItemCount(): Int = bookings.size
}