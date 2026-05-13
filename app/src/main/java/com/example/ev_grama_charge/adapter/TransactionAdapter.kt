package com.example.ev_grama_charge.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ev_grama_charge.databinding.ItemTransactionBinding
import com.example.ev_grama_charge.models.TransactionModel
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(
    private val transactionList:
    ArrayList<TransactionModel>
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding =
            ItemTransactionBinding.inflate(
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

        val transaction =
            transactionList[position]

        holder.binding.tvTransactionType.text =
            transaction.type

        holder.binding.tvTransactionStatus.text =
            transaction.status

        holder.binding.tvTransactionAmount.text =
            "+₹${transaction.amount.toInt()}"

        val formattedDate =
            SimpleDateFormat(
                "dd MMM yyyy",
                Locale.getDefault()
            ).format(Date(transaction.timestamp))

        holder.binding.tvTransactionDate.text =
            formattedDate

        // STATUS COLORS

        if (transaction.status == "SUCCESS") {

            holder.binding.tvTransactionStatus
                .setTextColor(
                    Color.parseColor("#10B981")
                )

        } else {

            holder.binding.tvTransactionStatus
                .setTextColor(
                    Color.parseColor("#EF4444")
                )
        }
    }

    override fun getItemCount(): Int {

        return transactionList.size
    }
}