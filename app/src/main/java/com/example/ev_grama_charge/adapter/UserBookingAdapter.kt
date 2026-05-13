package com.example.ev_grama_charge.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.databinding.ItemUserBookingBinding
import com.example.ev_grama_charge.models.BookingModel
import com.example.ev_grama_charge.ui.user.AddReviewActivity
import com.example.ev_grama_charge.ui.booking.BookingQrActivity

class UserBookingAdapter(
    private val bookingList:
    ArrayList<BookingModel>
) : RecyclerView.Adapter<UserBookingAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemUserBookingBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding =
            ItemUserBookingBinding.inflate(
                LayoutInflater.from(parent.context),
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
                    .setBackgroundResource(
                        R.drawable.bg_status_pending
                    )

                holder.binding.tvStatus
                    .setTextColor(
                        Color.WHITE
                    )
            }

            "ACCEPTED" -> {

                holder.binding.tvStatus
                    .setBackgroundResource(
                        R.drawable.bg_status_confirmed
                    )

                holder.binding.tvStatus
                    .setTextColor(
                        Color.WHITE
                    )
            }

            "REJECTED" -> {

                holder.binding.tvStatus
                    .setBackgroundResource(
                        R.drawable.bg_status_cancelled
                    )

                holder.binding.tvStatus
                    .setTextColor(
                        Color.WHITE
                    )
            }

            "COMPLETED" -> {

                holder.binding.tvStatus
                    .setBackgroundResource(
                        R.drawable.bg_card_gradient
                    )

                holder.binding.tvStatus
                    .setTextColor(
                        Color.WHITE
                    )
            }
        }

        // REVIEW BUTTON

        if (booking.status == "COMPLETED") {

            holder.binding.btnReview.visibility =
                android.view.View.VISIBLE

        } else {

            holder.binding.btnReview.visibility =
                android.view.View.GONE
        }

        // REVIEW CLICK

        holder.binding.btnReview
            .setOnClickListener {

                val intent =
                    Intent(
                        holder.itemView.context,
                        AddReviewActivity::class.java
                    )

                intent.putExtra(
                    "hostId",
                    booking.hostId
                )

                holder.itemView.context
                    .startActivity(intent)
            }
        // SHOW QR BUTTON

        if (booking.status == "ACCEPTED") {

            holder.binding.btnShowQr.visibility =
                android.view.View.VISIBLE

        } else {

            holder.binding.btnShowQr.visibility =
                android.view.View.GONE
        }

// QR CLICK

        holder.binding.btnShowQr
            .setOnClickListener {

                val intent =
                    Intent(
                        holder.itemView.context,
                        BookingQrActivity::class.java
                    )

                intent.putExtra(
                    "bookingId",
                    booking.bookingId
                )

                holder.itemView.context
                    .startActivity(intent)
            }
    }

    override fun getItemCount(): Int {

        return bookingList.size
    }
}