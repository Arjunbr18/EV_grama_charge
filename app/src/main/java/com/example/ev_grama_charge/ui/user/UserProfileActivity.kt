package com.example.ev_grama_charge.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ev_grama_charge.databinding.ActivityUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityUserProfileBinding.inflate(
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
                        EditUserProfileActivity::class.java
                    )
                )
            }
    }

    private fun loadProfile() {

        val userId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)

            .addSnapshotListener { document, _ ->

                if (
                    document == null ||
                    !document.exists()
                ) {

                    return@addSnapshotListener
                }

                binding.tvUserName.text =
                    document.getString("name")

                binding.tvEmail.text =
                    document.getString("email")

                binding.tvPhone.text =
                    document.getString("phone")

                binding.tvAddress.text =
                    document.getString("address")

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