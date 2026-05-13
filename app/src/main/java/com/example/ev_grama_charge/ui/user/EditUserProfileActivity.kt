package com.example.ev_grama_charge.ui.user

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
import com.example.ev_grama_charge.databinding.ActivityEditUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class EditUserProfileActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityEditUserProfileBinding

    private var selectedImageUri:
            Uri? = null

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
            ActivityEditUserProfileBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        loadProfile()

        setupClickListeners()
    }

    private fun setupClickListeners() {

        binding.imageProfile
            .setOnClickListener {

                checkPermission()
            }

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

        val userId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .get()

            .addOnSuccessListener { document ->

                binding.etName.setText(
                    document.getString(
                        "name"
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

        val userId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        val profileData =
            hashMapOf(

                "name" to
                        binding.etName.text
                            .toString(),

                "phone" to
                        binding.etPhone.text
                            .toString(),

                "address" to
                        binding.etAddress.text
                            .toString()
            )

        if (selectedImageUri != null) {

            val storageRef =
                FirebaseStorage.getInstance()
                    .reference
                    .child(
                        "user_profiles/$userId.jpg"
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
                                userId,
                                profileData
                            )
                        }
                }

        } else {

            updateFirestore(
                userId,
                profileData
            )
        }
    }

    private fun updateFirestore(
        userId: String,
        profileData:
        HashMap<String, String>
    ) {

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
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