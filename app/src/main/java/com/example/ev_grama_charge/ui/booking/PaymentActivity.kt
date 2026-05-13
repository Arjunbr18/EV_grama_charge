package com.example.ev_grama_charge.ui.booking

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.ui.payment.PaymentSuccessActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class PaymentActivity : AppCompatActivity() {

    private lateinit var tvHost: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvAmount: TextView

    private lateinit var btnPay: MaterialButton

    private var hostName = ""
    private var totalCost = 0.0
    private var date = ""
    private var time = ""
    private var duration = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_payment)

        tvHost = findViewById(R.id.tvHost)
        tvDate = findViewById(R.id.tvDate)
        tvTime = findViewById(R.id.tvTime)
        tvAmount = findViewById(R.id.tvAmount)

        btnPay = findViewById(R.id.btnPay)

        // RECEIVE DATA
        hostName =
            intent.getStringExtra("hostName") ?: "Host"

        totalCost =
            intent.getDoubleExtra("totalCost", 0.0)

        date =
            intent.getStringExtra("date") ?: "-"

        time =
            intent.getStringExtra("time") ?: "-"

        duration =
            intent.getDoubleExtra("duration", 0.0)

        // SET UI
        tvHost.text = hostName
        tvDate.text = date
        tvTime.text = time

        tvAmount.text =
            getString(
                R.string.payment_amount,
                totalCost
            )

        btnPay.setOnClickListener {
            processPayment()
        }
    }

    private fun processPayment() {

        Toast.makeText(
            this,
            getString(R.string.processing_payment),
            Toast.LENGTH_SHORT
        ).show()

        val transactionId =
            UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .uppercase()

        // BOOKING DATA
        val booking = hashMapOf(

            "hostName" to hostName,
            "totalCost" to totalCost,
            "date" to date,
            "time" to time,
            "duration" to duration,
            "status" to "CONFIRMED",
            "transactionId" to transactionId,
            "timestamp" to System.currentTimeMillis()
        )

        FirebaseFirestore.getInstance()
            .collection("bookings")
            .add(booking)

            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    getString(R.string.payment_success),
                    Toast.LENGTH_SHORT
                ).show()

                val intent =
                    Intent(
                        this,
                        PaymentSuccessActivity::class.java
                    )

                // SEND DATA
                intent.putExtra(
                    "hostName",
                    hostName
                )

                intent.putExtra(
                    "totalCost",
                    totalCost
                )

                intent.putExtra(
                    "date",
                    date
                )

                intent.putExtra(
                    "time",
                    time
                )

                intent.putExtra(
                    "duration",
                    duration
                )

                intent.putExtra(
                    "transactionId",
                    transactionId
                )

                startActivity(intent)

                // PREMIUM TRANSITION
                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )

                finish()
            }

            .addOnFailureListener {

                Toast.makeText(
                    this,
                    getString(R.string.payment_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun finish() {
        super.finish()

        // BACK TRANSITION
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }
}