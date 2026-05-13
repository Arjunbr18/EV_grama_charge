package com.example.ev_grama_charge.adapter

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ev_grama_charge.databinding.ItemHostChargerBinding
import com.example.ev_grama_charge.models.ChargerModel
import com.example.ev_grama_charge.ui.host.EditChargerActivity
import com.google.firebase.firestore.FirebaseFirestore

class HostChargerAdapter(
    private val chargerList:
    ArrayList<ChargerModel>
) : RecyclerView.Adapter<HostChargerAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemHostChargerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding =
            ItemHostChargerBinding.inflate(
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

        val charger = chargerList[position]

        holder.binding.tvChargerName.text =
            charger.chargerName

        holder.binding.tvChargerType.text =
            charger.chargerType

        holder.binding.tvConnector.text =
            charger.connectorType

        holder.binding.tvSpeed.text =
            charger.chargingSpeed

        holder.binding.tvPrice.text =
            "₹${charger.pricePerHour}/hr"

        holder.binding.switchCharger.isChecked =
            charger.available

        holder.binding.tvAvailability.text =
            if (charger.available)
                "Available"
            else
                "Unavailable"

        // AVAILABILITY SWITCH

        holder.binding.switchCharger
            .setOnCheckedChangeListener { _, isChecked ->

                FirebaseFirestore.getInstance()
                    .collection("chargers")
                    .document(charger.chargerId)
                    .update(
                        "available",
                        isChecked
                    )

                holder.binding.tvAvailability.text =
                    if (isChecked)
                        "Available"
                    else
                        "Unavailable"
            }

        // EDIT BUTTON

        holder.binding.btnEdit.setOnClickListener {

            val context =
                holder.itemView.context

            val intent =
                Intent(
                    context,
                    EditChargerActivity::class.java
                )

            intent.putExtra(
                "chargerId",
                charger.chargerId
            )

            intent.putExtra(
                "chargerName",
                charger.chargerName
            )

            intent.putExtra(
                "chargerType",
                charger.chargerType
            )

            intent.putExtra(
                "connectorType",
                charger.connectorType
            )

            intent.putExtra(
                "chargingSpeed",
                charger.chargingSpeed
            )

            intent.putExtra(
                "price",
                charger.pricePerHour
            )

            context.startActivity(intent)
        }

        // DELETE BUTTON

        holder.binding.btnDelete.setOnClickListener {

            AlertDialog.Builder(
                holder.itemView.context
            )
                .setTitle("Delete Charger")
                .setMessage(
                    "Are you sure you want to delete this charger?"
                )
                .setPositiveButton("Delete") { _, _ ->

                    FirebaseFirestore.getInstance()
                        .collection("chargers")
                        .document(charger.chargerId)
                        .delete()
                        .addOnSuccessListener {

                            Toast.makeText(
                                holder.itemView.context,
                                "Charger Deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .setNegativeButton(
                    "Cancel",
                    null
                )
                .show()
        }
    }

    override fun getItemCount(): Int {

        return chargerList.size
    }
}