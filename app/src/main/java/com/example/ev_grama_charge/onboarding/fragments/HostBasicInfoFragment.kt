package com.example.ev_grama_charge.onboarding.fragments

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.databinding.FragmentHostBasicInfoBinding
import com.example.ev_grama_charge.utils.HostOnboardingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class HostBasicInfoFragment : Fragment() {

    private var _binding: FragmentHostBasicInfoBinding? = null

    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null

    private lateinit var progressDialog: ProgressDialog

    private val galleryLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (
                result.resultCode == Activity.RESULT_OK &&
                result.data != null
            ) {

                selectedImageUri = result.data?.data

                Glide.with(requireContext())
                    .load(selectedImageUri)
                    .into(binding.imageProfile)
            }
        }

    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                openGallery()

            } else {

                Toast.makeText(
                    requireContext(),
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentHostBasicInfoBinding.inflate(
                inflater,
                container,
                false
            )

        progressDialog = ProgressDialog(requireContext())

        progressDialog.setMessage(
            "Uploading profile image..."
        )

        setupClickListeners()

        return binding.root
    }

    private fun setupClickListeners() {

        binding.imageProfile.setOnClickListener {

            checkPermissionAndOpenGallery()
        }

        binding.btnNext.setOnClickListener {

            val shopName =
                binding.etShopName.text.toString().trim()

            val ownerName =
                binding.etOwnerName.text.toString().trim()

            val phone =
                binding.etPhone.text.toString().trim()

            if (
                shopName.isEmpty() ||
                ownerName.isEmpty() ||
                phone.isEmpty()
            ) {

                Toast.makeText(
                    requireContext(),
                    "Fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            HostOnboardingData.shopName =
                shopName

            HostOnboardingData.ownerName =
                ownerName

            HostOnboardingData.phone =
                phone

            if (selectedImageUri != null) {

                uploadProfileImage()

            } else {

                moveToNextPage()
            }
        }
    }

    private fun checkPermissionAndOpenGallery() {

        val permission = if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        ) {

            Manifest.permission.READ_MEDIA_IMAGES

        } else {

            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            openGallery()

        } else {

            permissionLauncher.launch(permission)
        }
    }

    private fun openGallery() {

        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        galleryLauncher.launch(intent)
    }

    private fun uploadProfileImage() {

        progressDialog.show()

        val userId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        val storageReference =
            FirebaseStorage.getInstance()
                .reference
                .child(
                    "host_profiles/$userId.jpg"
                )

        storageReference.putFile(selectedImageUri!!)
            .addOnSuccessListener {

                storageReference.downloadUrl
                    .addOnSuccessListener { uri ->

                        progressDialog.dismiss()

                        HostOnboardingData.profileImageUrl =
                            uri.toString()

                        Toast.makeText(
                            requireContext(),
                            "Image Uploaded",
                            Toast.LENGTH_SHORT
                        ).show()

                        moveToNextPage()
                    }
            }
            .addOnFailureListener {

                progressDialog.dismiss()

                Toast.makeText(
                    requireContext(),
                    "Upload Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun moveToNextPage() {

        val viewPager =
            activity?.findViewById<ViewPager2>(
                R.id.viewPager
            )

        viewPager?.currentItem = 1
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}