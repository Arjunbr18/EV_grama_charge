package com.example.ev_grama_charge.ui.booking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.models.BookingModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.UUID

class BookingActivity : AppCompatActivity() {

    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var etDuration: TextInputEditText
    private lateinit var tvCost: TextView
    private lateinit var btnConfirm: MaterialButton

    private var selectedDate = ""
    private var selectedTime = ""

    private var pricePerHour = 0.0

    private var hostName = ""

    private var hostId = ""

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_booking
        )

        tvDate =
            findViewById(R.id.tvDate)

        tvTime =
            findViewById(R.id.tvTime)

        etDuration =
            findViewById(R.id.etDuration)

        tvCost =
            findViewById(R.id.tvCost)

        btnConfirm =
            findViewById(R.id.btnConfirm)

        // RECEIVE DATA

        pricePerHour =
            intent.getDoubleExtra(
                "price",
                0.0
            )

        hostName =
            intent.getStringExtra(
                "name"
            ) ?: "Charging Station"

        hostId =
            intent.getStringExtra(
                "hostId"
            ) ?: ""

        tvDate.setOnClickListener {

            pickDate()
        }

        tvTime.setOnClickListener {

            pickTime()
        }

        etDuration.addTextChangedListener(

            object : TextWatcher {

                override fun afterTextChanged(
                    s: Editable?
                ) {

                    calculateCost()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {}
            }
        )

        btnConfirm.setOnClickListener {

            validateAndProceed()
        }
    }

    private fun pickDate() {

        val cal =
            Calendar.getInstance()

        DatePickerDialog(

            this,

            { _, year, month, day ->

                selectedDate =
                    "$day/${month + 1}/$year"

                tvDate.text =
                    selectedDate
            },

            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)

        ).show()
    }

    private fun pickTime() {

        val cal =
            Calendar.getInstance()

        TimePickerDialog(

            this,

            { _, hour, minute ->

                val selectedCal =
                    Calendar.getInstance()

                selectedCal.set(
                    Calendar.HOUR_OF_DAY,
                    hour
                )

                selectedCal.set(
                    Calendar.MINUTE,
                    minute
                )

                val now =
                    Calendar.getInstance()

                if (

                    selectedDate ==
                    getTodayDate()

                    &&

                    selectedCal.before(now)
                ) {

                    Toast.makeText(

                        this,

                        "Cannot select past time",

                        Toast.LENGTH_SHORT

                    ).show()

                    return@TimePickerDialog
                }

                selectedTime =
                    String.format(
                        "%02d:%02d",
                        hour,
                        minute
                    )

                tvTime.text =
                    selectedTime
            },

            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true

        ).show()
    }

    private fun getTodayDate(): String {

        val cal =
            Calendar.getInstance()

        return "${cal.get(Calendar.DAY_OF_MONTH)}/" +
                "${cal.get(Calendar.MONTH) + 1}/" +
                "${cal.get(Calendar.YEAR)}"
    }

    private fun calculateCost() {

        val duration =

            etDuration.text.toString()
                .toDoubleOrNull()

                ?: 0.0

        val total =
            duration * pricePerHour

        tvCost.text =
            getString(
                R.string.booking_cost,
                total
            )
    }

    private fun validateAndProceed() {

        val duration =

            etDuration.text.toString()
                .toDoubleOrNull()

        if (selectedDate.isEmpty()) {

            Toast.makeText(
                this,
                "Select date",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (selectedTime.isEmpty()) {

            Toast.makeText(
                this,
                "Select time",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (
            duration == null ||
            duration <= 0
        ) {

            Toast.makeText(
                this,
                "Enter valid duration",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        checkSlotAvailability()
    }

    private fun checkSlotAvailability() {

        FirebaseFirestore.getInstance()

            .collection("bookings")

            .whereEqualTo(
                "hostId",
                hostId
            )

            .whereEqualTo(
                "bookingDate",
                selectedDate
            )

            .whereEqualTo(
                "bookingTime",
                selectedTime
            )

            .whereEqualTo(
                "status",
                "ACCEPTED"
            )

            .get()

            .addOnSuccessListener { result ->

                if (!result.isEmpty) {

                    Toast.makeText(

                        this,

                        "This slot is already booked",

                        Toast.LENGTH_LONG

                    ).show()

                } else {

                    createBooking()
                }
            }

            .addOnFailureListener {

                Toast.makeText(

                    this,

                    "Failed to check slot availability",

                    Toast.LENGTH_SHORT

                ).show()
            }
    }

    private fun createBooking() {

        val userId =

            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()

            .collection("users")

            .document(userId)

            .get()

            .addOnSuccessListener { userDoc ->

                val userName =

                    userDoc.getString(
                        "name"
                    ) ?: "Customer"

                val userPhone =

                    userDoc.getString(
                        "phone"
                    ) ?: ""

                val duration =
                    etDuration.text.toString()

                val totalCost =

                    (
                            duration.toDoubleOrNull()
                                ?: 0.0
                            ) * pricePerHour

                val bookingId =
                    UUID.randomUUID().toString()

                val booking = BookingModel(

                    bookingId = bookingId,

                    userId = userId,

                    hostId = hostId,

                    chargerId = "",

                    chargerName = hostName,

                    userName = userName,

                    userPhone = userPhone,

                    bookingDate = selectedDate,

                    bookingTime = selectedTime,

                    duration = "${duration} Hour(s)",

                    amount = totalCost.toString(),

                    status = "PENDING"
                )

                FirebaseFirestore.getInstance()

                    .collection("bookings")

                    .document(bookingId)

                    .set(booking)

                    .addOnSuccessListener {

                        Toast.makeText(

                            this,

                            "Booking Created",

                            Toast.LENGTH_SHORT

                        ).show()

                        openPaymentScreen(
                            totalCost
                        )
                    }

                    .addOnFailureListener {

                        Toast.makeText(

                            this,

                            "Booking Failed",

                            Toast.LENGTH_SHORT

                        ).show()
                    }
            }
    }

    private fun openPaymentScreen(
        totalCost: Double
    ) {

        val intent =
            Intent(
                this,
                PaymentActivity::class.java
            )

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
            selectedDate
        )

        intent.putExtra(
            "time",
            selectedTime
        )

        startActivity(intent)

        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }

    override fun finish() {

        super.finish()

        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }
}