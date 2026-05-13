package com.example.ev_grama_charge.ui.host

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.adapter.HostChargerAdapter
import com.example.ev_grama_charge.databinding.ActivityHostHomeBinding
import com.example.ev_grama_charge.models.ChargerModel
import com.example.ev_grama_charge.models.HostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HostHomeActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityHostHomeBinding

    private lateinit var chargerAdapter:
            HostChargerAdapter

    private val chargerList =
        ArrayList<ChargerModel>()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityHostHomeBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        setupRecyclerView()

        setupClickListeners()

        loadHostProfile()

        loadChargers()

        loadAnalytics()
    }

    private fun setupRecyclerView() {

        chargerAdapter =
            HostChargerAdapter(
                chargerList
            )

        binding.recyclerChargers.apply {

            layoutManager =
                LinearLayoutManager(
                    this@HostHomeActivity
                )

            adapter =
                chargerAdapter

            setHasFixedSize(false)

            isNestedScrollingEnabled =
                false
        }
    }

    private fun setupClickListeners() {

        binding.btnAddCharger
            .setOnClickListener {

                startActivity(

                    Intent(
                        this,
                        AddChargerActivity::class.java
                    )
                )
            }

        binding.btnWallet
            .setOnClickListener {

                startActivity(

                    Intent(
                        this,
                        HostWalletActivity::class.java
                    )
                )
            }

        binding.btnAvailability
            .setOnClickListener {

                startActivity(

                    Intent(
                        this,
                        HostAvailabilityActivity::class.java
                    )
                )
            }

        binding.btnReviews
            .setOnClickListener {

                startActivity(

                    Intent(
                        this,
                        HostReviewsActivity::class.java
                    )
                )
            }

        binding.layoutProfile
            .setOnClickListener {

                startActivity(

                    Intent(
                        this,
                        HostProfileActivity::class.java
                    )
                )
            }

        findViewById<View>(
            R.id.cardBookings
        ).setOnClickListener {

            startActivity(

                Intent(
                    this,
                    HostBookingsActivity::class.java
                )
            )

            overridePendingTransition(

                R.anim.slide_in_right,

                R.anim.slide_out_left
            )
        }
    }

    private fun loadHostProfile() {

        val userId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()

            .collection("hosts")

            .document(userId)

            .get()

            .addOnSuccessListener { document ->

                val host =
                    document.toObject(
                        HostModel::class.java
                    )

                host?.let {

                    binding.tvHostName.text =
                        it.ownerName

                    binding.tvShopName.text =
                        it.shopName

                    Glide.with(this)

                        .load(it.profileImage)

                        .into(binding.imageHost)
                }
            }
    }

    private fun loadChargers() {

        val hostId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()

            .collection("chargers")

            .whereEqualTo(
                "hostId",
                hostId
            )

            .addSnapshotListener { value, error ->

                if (error != null) {

                    return@addSnapshotListener
                }

                chargerList.clear()

                if (
                    value == null ||
                    value.isEmpty
                ) {

                    binding.tvEmptyChargers.visibility =
                        View.VISIBLE

                } else {

                    binding.tvEmptyChargers.visibility =
                        View.GONE

                    for (document in value.documents) {

                        val charger =
                            document.toObject(
                                ChargerModel::class.java
                            )

                        charger?.let {

                            chargerList.add(it)
                        }
                    }
                }

                chargerAdapter.notifyDataSetChanged()

                // IMPORTANT FIX

                binding.recyclerChargers.post {

                    binding.recyclerChargers
                        .requestLayout()
                }
            }
    }

    private fun loadAnalytics() {

        val hostId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()

            .collection("chargers")

            .whereEqualTo(
                "hostId",
                hostId
            )

            .addSnapshotListener { value, _ ->

                if (value == null)
                    return@addSnapshotListener

                val totalChargers =
                    value.documents.size

                var availableChargers = 0

                var estimatedRevenue = 0

                for (document in value.documents) {

                    val charger =
                        document.toObject(
                            ChargerModel::class.java
                        )

                    charger?.let {

                        if (it.available) {

                            availableChargers++
                        }

                        estimatedRevenue +=

                            it.pricePerHour
                                .toIntOrNull()
                                ?: 0
                    }
                }

                binding.tvTotalChargers.text =
                    totalChargers.toString()

                binding.tvAvailableChargers.text =
                    availableChargers.toString()

                binding.tvRevenue.text =
                    "₹$estimatedRevenue"
            }
    }
}