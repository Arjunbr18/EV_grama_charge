package com.example.ev_grama_charge.ui.user

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.databinding.ActivityAddReviewBinding
import com.example.ev_grama_charge.models.ReviewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class AddReviewActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityAddReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityAddReviewBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        setupSubmitButton()
    }

    private fun setupSubmitButton() {

        binding.btnSubmitReview
            .setOnClickListener {

                submitReview()
            }
    }

    private fun submitReview() {

        val hostId =
            intent.getStringExtra("hostId")
                ?: return

        val userId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        val userName =
            FirebaseAuth.getInstance()
                .currentUser?.displayName
                ?: "EV User"

        val rating =
            binding.ratingBar.rating

        val reviewText =
            binding.etReview.text
                .toString()
                .trim()

        if (reviewText.isEmpty()) {

            Toast.makeText(
                this,
                "Write review first",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val reviewId =
            UUID.randomUUID().toString()

        val review =
            ReviewModel(

                reviewId = reviewId,

                hostId = hostId,

                userId = userId,

                userName = userName,

                rating = rating,

                review = reviewText
            )

        FirebaseFirestore.getInstance()
            .collection("reviews")
            .document(reviewId)
            .set(review)

            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Review Submitted",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }

            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed to submit review",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}