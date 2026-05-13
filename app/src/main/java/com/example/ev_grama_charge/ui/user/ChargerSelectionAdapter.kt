package com.example.ev_grama_charge.ui.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.models.ChargerModel
import com.google.android.material.card.MaterialCardView

class ChargerSelectionAdapter(

    private val list:
    ArrayList<ChargerModel>,

    private val onSelect:
        (ChargerModel) -> Unit

) : RecyclerView.Adapter<
        ChargerSelectionAdapter.ViewHolder>() {

    private var selectedPosition =
        0

    class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        val tvChargerName:
                TextView =
            view.findViewById(
                R.id.tvChargerName
            )

        val tvChargerType:
                TextView =
            view.findViewById(
                R.id.tvChargerType
            )

        val tvConnector:
                TextView =
            view.findViewById(
                R.id.tvConnector
            )

        val tvSpeed:
                TextView =
            view.findViewById(
                R.id.tvSpeed
            )

        val tvPrice:
                TextView =
            view.findViewById(
                R.id.tvPricePerHour
            )

        val cardView:
                MaterialCardView =
            view as MaterialCardView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.item_charger,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val charger =
            list[position]

        // SET DATA

        holder.tvChargerName.text =
            charger.chargerName

        holder.tvChargerType.text =
            holder.itemView.context.getString(
                R.string.charger_type_text,
                charger.chargerType
            )

        holder.tvConnector.text =
            holder.itemView.context.getString(
                R.string.connector_text,
                charger.connectorType
            )

        holder.tvSpeed.text =
            holder.itemView.context.getString(
                R.string.speed_text,
                charger.chargingSpeed
            )

        holder.tvPrice.text =
            holder.itemView.context.getString(
                R.string.price_per_hour_text,
                charger.pricePerHour
            )

        // RESET UI FIRST

        holder.cardView.strokeWidth =
            1

        holder.cardView.strokeColor =
            "#EEEEEE".toColorInt()

        holder.cardView.cardElevation =
            4f

        holder.itemView.alpha =
            0.9f

        // SELECTED UI

        if (selectedPosition == position) {

            holder.cardView.strokeWidth =
                6

            holder.cardView.strokeColor =
                "#10B981".toColorInt()

            holder.cardView.cardElevation =
                12f

            holder.itemView.alpha =
                1f
        }

        // CLICK

        holder.itemView
            .setOnClickListener {

                val currentPosition =
                    holder.adapterPosition

                if (
                    currentPosition ==
                    RecyclerView.NO_POSITION
                ) {
                    return@setOnClickListener
                }

                val previousPosition =
                    selectedPosition

                selectedPosition =
                    currentPosition

                notifyItemChanged(
                    previousPosition
                )

                notifyItemChanged(
                    selectedPosition
                )

                Toast.makeText(

                    holder.itemView.context,

                    "${list[currentPosition].chargerName} Selected",

                    Toast.LENGTH_SHORT

                ).show()

                onSelect(
                    list[currentPosition]
                )
            }
    }

    override fun getItemCount():
            Int = list.size
}