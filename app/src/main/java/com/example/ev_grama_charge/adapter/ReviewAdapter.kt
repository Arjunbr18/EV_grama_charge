package com.example.ev_grama_charge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ev_grama_charge.databinding.ItemReviewBinding
import com.example.ev_grama_charge.models.ReviewModel

class ReviewAdapter(
    private val reviewList:
    ArrayList<ReviewModel>
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemReviewBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding =
            ItemReviewBinding.inflate(
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

        val review =
            reviewList[position]

        holder.binding.tvUserName.text =
            review.userName

        holder.binding.tvReview.text =
            review.review

        holder.binding.ratingBarReview.rating =
            review.rating

        // USER INITIAL

        if (review.userName.isNotEmpty()) {

            holder.binding.tvUserInitial.text =
                review.userName[0]
                    .toString()
                    .uppercase()
        }
    }

    override fun getItemCount(): Int {

        return reviewList.size
    }
}