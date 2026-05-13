package com.example.ev_grama_charge.onboarding.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ev_grama_charge.databinding.FragmentHostLocationBinding
import com.example.ev_grama_charge.models.HostModel
import com.example.ev_grama_charge.ui.host.HostHomeActivity
import com.example.ev_grama_charge.utils.HostOnboardingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HostLocationFragment : Fragment() {

    private var _binding: FragmentHostLocationBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentHostLocationBinding.inflate(
                inflater,
                container,
                false
            )

        setupClickListeners()

        return binding.root
    }

    private fun setupClickListeners() {

        binding.btnFinish.setOnClickListener {

            val address =
                binding.etAddress.text.toString().trim()

            if (address.isEmpty()) {

                Toast.makeText(
                    requireContext(),
                    "Enter address",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                HostOnboardingData.address =
                    address

                saveHostProfile()
            }
        }
    }

    private fun saveHostProfile() {

        val userId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        val hostModel = HostModel(

            hostId = userId,

            shopName =
                HostOnboardingData.shopName,

            ownerName =
                HostOnboardingData.ownerName,

            phone =
                HostOnboardingData.phone,

            address =
                HostOnboardingData.address,

            profileImage =
                HostOnboardingData.profileImageUrl
        )

        FirebaseFirestore.getInstance()
            .collection("hosts")
            .document(userId)
            .set(hostModel)
            .addOnSuccessListener {

                Toast.makeText(
                    requireContext(),
                    "Host Profile Created",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(
                        requireContext(),
                        HostHomeActivity::class.java
                    )
                )

                requireActivity().finish()
            }
            .addOnFailureListener {

                Toast.makeText(
                    requireContext(),
                    "Failed To Save",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}