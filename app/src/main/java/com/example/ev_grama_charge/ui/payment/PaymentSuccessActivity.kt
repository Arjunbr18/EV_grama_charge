package com.example.ev_grama_charge.ui.payment

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.ui.booking.BookingHistoryActivity
import com.google.android.material.button.MaterialButton

class PaymentSuccessActivity : AppCompatActivity() {

    private lateinit var tvHost: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvAmount: TextView
    private lateinit var tvTransactionId: TextView

    private lateinit var btnDone: MaterialButton
    private lateinit var btnShare: MaterialButton

    private var hostName = ""
    private var amount = 0.0
    private var date = ""
    private var time = ""
    private var transactionId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        tvHost = findViewById(R.id.tvHost)
        tvDate = findViewById(R.id.tvDate)
        tvTime = findViewById(R.id.tvTime)
        tvAmount = findViewById(R.id.tvAmount)
        tvTransactionId =
            findViewById(R.id.tvTransactionId)

        btnDone = findViewById(R.id.btnDone)
        btnShare = findViewById(R.id.btnShare)

        hostName =
            intent.getStringExtra("hostName") ?: "Host"

        amount =
            intent.getDoubleExtra("totalCost", 0.0)

        date =
            intent.getStringExtra("date") ?: "-"

        time =
            intent.getStringExtra("time") ?: "-"

        transactionId =
            intent.getStringExtra("transactionId") ?: "-"

        tvHost.text = hostName
        tvDate.text = date
        tvTime.text = time
        tvTransactionId.text = transactionId

        tvAmount.text =
            getString(
                R.string.payment_amount,
                amount
            )

        btnDone.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    BookingHistoryActivity::class.java
                )
            )

            finish()
        }

        btnShare.setOnClickListener {
            shareReceipt()
        }
    }

    private fun shareReceipt() {

        val message = """

EV-Grama Charge Receipt

Host: $hostName
Date: $date
Time: $time
Amount: ₹$amount

Transaction ID:
$transactionId

Status: CONFIRMED

        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "text/plain"

        intent.putExtra(
            Intent.EXTRA_TEXT,
            message
        )

        startActivity(
            Intent.createChooser(
                intent,
                "Share Receipt"
            )
        )
    }
}