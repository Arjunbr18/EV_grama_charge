package com.example.ev_grama_charge.ui.host

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ev_grama_charge.adapter.TransactionAdapter
import com.example.ev_grama_charge.databinding.ActivityHostWalletBinding
import com.example.ev_grama_charge.models.TransactionModel
import com.example.ev_grama_charge.models.WalletModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HostWalletActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityHostWalletBinding

    private lateinit var transactionAdapter:
            TransactionAdapter

    private val transactionList =
        ArrayList<TransactionModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityHostWalletBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        setupRecyclerView()

        loadWallet()

        loadTransactions()
    }

    private fun setupRecyclerView() {

        transactionAdapter =
            TransactionAdapter(transactionList)

        binding.recyclerTransactions.apply {

            layoutManager =
                LinearLayoutManager(
                    this@HostWalletActivity
                )

            adapter =
                transactionAdapter
        }
    }

    private fun loadWallet() {

        val hostId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("wallets")
            .document(hostId)
            .addSnapshotListener { value, error ->

                if (
                    error != null ||
                    value == null ||
                    !value.exists()
                ) {

                    return@addSnapshotListener
                }

                val wallet =
                    value.toObject(
                        WalletModel::class.java
                    )

                wallet?.let {

                    binding.tvWalletBalance.text =
                        "₹${it.walletBalance.toInt()}"

                    binding.tvTotalEarnings.text =
                        "₹${it.totalEarnings.toInt()}"

                    binding.tvPendingPayout.text =
                        "₹${it.pendingPayout.toInt()}"
                }
            }
    }

    private fun loadTransactions() {

        val hostId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("transactions")
            .whereEqualTo("hostId", hostId)
            .addSnapshotListener { value, error ->

                if (error != null) {

                    return@addSnapshotListener
                }

                transactionList.clear()

                if (
                    value == null ||
                    value.isEmpty
                ) {

                    binding.tvEmptyTransactions.visibility =
                        View.VISIBLE

                } else {

                    binding.tvEmptyTransactions.visibility =
                        View.GONE

                    for (document in value.documents) {

                        val transaction =
                            document.toObject(
                                TransactionModel::class.java
                            )

                        transaction?.let {

                            transactionList.add(it)
                        }
                    }
                }

                transactionAdapter.notifyDataSetChanged()
            }
    }
}