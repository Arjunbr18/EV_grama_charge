package com.example.ev_grama_charge.ui.host

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ev_grama_charge.adapter.ReviewAdapter
import com.example.ev_grama_charge.databinding.ActivityHostReviewsBinding
import com.example.ev_grama_charge.models.ReviewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HostReviewsActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityHostReviewsBinding

    private lateinit var reviewAdapter:
            ReviewAdapter

    private val reviewList =
        ArrayList<ReviewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityHostReviewsBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        setupRecyclerView()

        loadReviews()
    }

    private fun setupRecyclerView() {

        reviewAdapter =
            ReviewAdapter(reviewList)

        binding.recyclerReviews.apply {

            layoutManager =
                LinearLayoutManager(
                    this@HostReviewsActivity
                )

            adapter =
                reviewAdapter
        }
    }

    private fun loadReviews() {

        val hostId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("reviews")
            .whereEqualTo("hostId", hostId)

            .addSnapshotListener { value, error ->

                if (error != null) {

                    return@addSnapshotListener
                }

                reviewList.clear()

                if (
                    value == null ||
                    value.isEmpty
                ) {

                    binding.tvEmptyReviews.visibility =
                        View.VISIBLE

                } else {

                    binding.tvEmptyReviews.visibility =
                        View.GONE

                    var totalRating = 0f

                    for (document in value.documents) {

                        val review =
                            document.toObject(
                                ReviewModel::class.java
                            )

                        review?.let {

                            reviewList.add(it)

                            totalRating +=
                                it.rating
                        }
                    }

                    val averageRating =
                        totalRating /
                                reviewList.size

                    binding.tvAverageRating.text =
                        String.format(
                            "%.1f",
                            averageRating
                        )

                    binding.tvTotalReviews.text =
                        reviewList.size.toString()
                }

                reviewAdapter.notifyDataSetChanged()
            }
    }
}