package com.example.ev_grama_charge.ui.host

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.ev_grama_charge.databinding.ActivityEditHostProfileBinding
import com.example.ev_grama_charge.ui.maps.LocationPickerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class EditHostProfileActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityEditHostProfileBinding

    private var selectedImageUri:
            Uri? = null

    private var selectedLatitude = 0.0

    private var selectedLongitude = 0.0

    // IMAGE PICKER

    private val galleryLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (
                result.resultCode ==
                Activity.RESULT_OK
            ) {

                selectedImageUri =
                    result.data?.data

                Glide.with(this)
                    .load(selectedImageUri)
                    .into(binding.imageProfile)
            }
        }

    // LOCATION PICKER

    private val locationLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (
                result.resultCode ==
                Activity.RESULT_OK
            ) {

                selectedLatitude =
                    result.data?.getDoubleExtra(
                        "latitude",
                        0.0
                    ) ?: 0.0

                selectedLongitude =
                    result.data?.getDoubleExtra(
                        "longitude",
                        0.0
                    ) ?: 0.0

                binding.tvLocation.text =
                    "Lat: $selectedLatitude\nLng: $selectedLongitude"
            }
        }

    // STORAGE PERMISSION

    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                openGallery()

            } else {

                Toast.makeText(
                    this,
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityEditHostProfileBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        loadProfile()

        setupClickListeners()
    }

    private fun setupClickListeners() {

        // PROFILE IMAGE

        binding.imageProfile
            .setOnClickListener {

                checkPermission()
            }

        // LOCATION BUTTON

        binding.btnSelectLocation
            .setOnClickListener {

                val intent =
                    Intent(
                        this,
                        LocationPickerActivity::class.java
                    )

                locationLauncher.launch(intent)
            }

        // SAVE PROFILE

        binding.btnSaveProfile
            .setOnClickListener {

                saveProfile()
            }
    }

    private fun checkPermission() {

        val permission = if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {

            Manifest.permission.READ_MEDIA_IMAGES

        } else {

            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            openGallery()

        } else {

            permissionLauncher.launch(
                permission
            )
        }
    }

    private fun openGallery() {

        val intent =
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media
                    .EXTERNAL_CONTENT_URI
            )

        galleryLauncher.launch(intent)
    }

    private fun loadProfile() {

        val hostId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("hosts")
            .document(hostId)
            .get()

            .addOnSuccessListener { document ->

                binding.etShopName.setText(
                    document.getString(
                        "shopName"
                    )
                )

                binding.etOwnerName.setText(
                    document.getString(
                        "ownerName"
                    )
                )

                binding.etPhone.setText(
                    document.getString(
                        "phone"
                    )
                )

                binding.etAddress.setText(
                    document.getString(
                        "address"
                    )
                )

                // LOAD LOCATION

                selectedLatitude =
                    document.getString(
                        "profileLatitude"
                    )?.toDoubleOrNull() ?: 0.0

                selectedLongitude =
                    document.getString(
                        "profileLongitude"
                    )?.toDoubleOrNull() ?: 0.0

                if (
                    selectedLatitude != 0.0 &&
                    selectedLongitude != 0.0
                ) {

                    binding.tvLocation.text =
                        "Lat: $selectedLatitude\nLng: $selectedLongitude"
                }

                Glide.with(this)
                    .load(
                        document.getString(
                            "profileImage"
                        )
                    )
                    .into(binding.imageProfile)
            }
    }

    private fun saveProfile() {

        val hostId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        val profileData =
            hashMapOf(

                "shopName" to
                        binding.etShopName.text
                            .toString(),

                "ownerName" to
                        binding.etOwnerName.text
                            .toString(),

                "phone" to
                        binding.etPhone.text
                            .toString(),

                "address" to
                        binding.etAddress.text
                            .toString(),

                "profileLatitude" to
                        selectedLatitude.toString(),

                "profileLongitude" to
                        selectedLongitude.toString()
            )

        if (selectedImageUri != null) {

            val storageRef =
                FirebaseStorage.getInstance()
                    .reference
                    .child(
                        "host_profiles/$hostId.jpg"
                    )

            storageRef.putFile(
                selectedImageUri!!
            )

                .addOnSuccessListener {

                    storageRef.downloadUrl
                        .addOnSuccessListener { uri ->

                            profileData[
                                "profileImage"
                            ] =
                                uri.toString()

                            updateFirestore(
                                hostId,
                                profileData
                            )
                        }
                }

        } else {

            updateFirestore(
                hostId,
                profileData
            )
        }
    }

    private fun updateFirestore(
        hostId: String,
        profileData:
        HashMap<String, String>
    ) {

        FirebaseFirestore.getInstance()
            .collection("hosts")
            .document(hostId)
            .update(profileData as Map<String, Any>)

            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Profile Updated",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }

            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed to update profile",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}