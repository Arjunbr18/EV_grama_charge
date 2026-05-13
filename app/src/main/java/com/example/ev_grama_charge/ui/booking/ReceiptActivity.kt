package com.example.ev_grama_charge.ui.booking

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.R
import com.google.android.material.button.MaterialButton

class ReceiptActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_receipt)

        val host =
            intent.getStringExtra("host") ?: "Host"

        val date =
            intent.getStringExtra("date") ?: "-"

        val time =
            intent.getStringExtra("time") ?: "-"

        val durationValue =
            intent.getDoubleExtra("duration", 0.0)

        val duration =
            String.format("%.1f", durationValue)

        val amountValue =
            intent.getDoubleExtra("amount", 0.0)

        val amount =
            String.format("%.2f", amountValue)

        val transactionId =
            intent.getStringExtra("transactionId")
                ?: "N/A"

        val tvReceipt =
            findViewById<TextView>(R.id.tvReceipt)

        val btnShare =
            findViewById<MaterialButton>(R.id.btnShare)

        // PREMIUM RECEIPT
        val receiptText = """

EV-Grama Charge Receipt

━━━━━━━━━━━━━━━━━━

Charging Host
$host

Booking Date
$date

Charging Time
$time

Charging Duration
$duration hrs

Transaction ID
$transactionId

Payment Status
CONFIRMED

Amount Paid
₹$amount

━━━━━━━━━━━━━━━━━━

Thank you for using
EV-Grama Charge

Community Powered EV Charging

        """.trimIndent()

        tvReceipt.text = receiptText

        btnShare.setOnClickListener {

            val share =
                Intent(Intent.ACTION_SEND)

            share.type = "text/plain"

            share.putExtra(
                Intent.EXTRA_TEXT,
                receiptText
            )

            startActivity(
                Intent.createChooser(
                    share,
                    "Share Receipt"
                )
            )

            // SHARE TRANSITION
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
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