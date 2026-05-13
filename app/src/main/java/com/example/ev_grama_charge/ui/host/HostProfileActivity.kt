package com.example.ev_grama_charge.ui.host

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ev_grama_charge.databinding.ActivityHostProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HostProfileActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityHostProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityHostProfileBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        setupClickListeners()

        loadProfile()
    }

    private fun setupClickListeners() {

        binding.btnEditProfile
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        EditHostProfileActivity::class.java
                    )
                )
            }
    }

    private fun loadProfile() {

        val hostId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("hosts")
            .document(hostId)
            .addSnapshotListener { document, _ ->

                if (
                    document == null ||
                    !document.exists()
                ) {

                    return@addSnapshotListener
                }

                binding.tvOwnerName.text =
                    document.getString(
                        "ownerName"
                    )

                binding.tvShopName.text =
                    document.getString(
                        "shopName"
                    )

                binding.tvPhone.text =
                    document.getString(
                        "phone"
                    )

                binding.tvAddress.text =
                    document.getString(
                        "address"
                    )

                Glide.with(this)
                    .load(
                        document.getString(
                            "profileImage"
                        )
                    )
                    .into(binding.imageProfile)
            }
    }
}