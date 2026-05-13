package com.example.ev_grama_charge.ui.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.data.model.Host

class HostAdapter(
    private val list: List<Host>,
    private val onClick: (Host) -> Unit
) : RecyclerView.Adapter<HostAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val rating: TextView = view.findViewById(R.id.tvRating)
        val distance: TextView = view.findViewById(R.id.tvDistance)
        val status: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_host, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val host = list[position]

        holder.name.text =
            host.name

        holder.price.text =
            "₹${host.price}/hr"

        holder.rating.text =
            "⭐ ${host.rating}"

        holder.distance.text =
            String.format(
                "%.1f km away",
                host.distance
            )

        if (host.isAvailable) {

            holder.status.text =
                "Available"

            holder.status.setBackgroundResource(
                R.drawable.bg_status_confirmed
            )

            holder.status.alpha = 1f

            holder.itemView.alpha = 1f

            holder.itemView.isEnabled = true

        } else {

            holder.status.text =
                "Busy"

            holder.status.setBackgroundResource(
                R.drawable.bg_status_busy
            )

            // DIM EFFECT
            holder.itemView.alpha = 0.65f

            holder.itemView.isEnabled = false
        }

        // CLICK ONLY IF AVAILABLE
        holder.itemView.setOnClickListener {

            if (host.isAvailable) {
                onClick(host)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}