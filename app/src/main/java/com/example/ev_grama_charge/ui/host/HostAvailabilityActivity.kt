package com.example.ev_grama_charge.ui.host

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.databinding.ActivityHostAvailabilityBinding
import com.example.ev_grama_charge.models.AvailabilityModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class HostAvailabilityActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityHostAvailabilityBinding

    private var openingTime = ""

    private var closingTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityHostAvailabilityBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        setupTimePickers()

        loadAvailability()

        setupSaveButton()
    }

    private fun setupTimePickers() {

        binding.tvOpeningTime.setOnClickListener {

            showTimePicker(true)
        }

        binding.tvClosingTime.setOnClickListener {

            showTimePicker(false)
        }
    }

    private fun showTimePicker(
        isOpening: Boolean
    ) {

        val calendar =
            Calendar.getInstance()

        val hour =
            calendar.get(Calendar.HOUR_OF_DAY)

        val minute =
            calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->

                val time =
                    String.format(
                        "%02d:%02d",
                        selectedHour,
                        selectedMinute
                    )

                if (isOpening) {

                    openingTime = time

                    binding.tvOpeningTime.text =
                        time

                } else {

                    closingTime = time

                    binding.tvClosingTime.text =
                        time
                }
            },
            hour,
            minute,
            true
        ).show()
    }

    private fun setupSaveButton() {

        binding.btnSaveAvailability
            .setOnClickListener {

                saveAvailability()
            }
    }

    private fun saveAvailability() {

        val hostId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        if (
            openingTime.isEmpty() ||
            closingTime.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Select opening and closing time",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val workingDays =
            ArrayList<String>()

        val checkBoxes =
            listOf(

                binding.checkMonday,
                binding.checkTuesday,
                binding.checkWednesday,
                binding.checkThursday,
                binding.checkFriday,
                binding.checkSaturday,
                binding.checkSunday
            )

        for (checkBox in checkBoxes) {

            if (checkBox.isChecked) {

                workingDays.add(
                    checkBox.text.toString()
                )
            }
        }

        val availability =
            AvailabilityModel(

                hostId = hostId,

                openingTime = openingTime,

                closingTime = closingTime,

                maintenanceMode =
                    binding.switchMaintenance.isChecked,

                workingDays = workingDays
            )

        FirebaseFirestore.getInstance()
            .collection("availability")
            .document(hostId)
            .set(availability)

            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Availability Saved",
                    Toast.LENGTH_SHORT
                ).show()
            }

            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed to save availability",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun loadAvailability() {

        val hostId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("availability")
            .document(hostId)
            .get()

            .addOnSuccessListener { document ->

                if (!document.exists()) {

                    return@addOnSuccessListener
                }

                val availability =
                    document.toObject(
                        AvailabilityModel::class.java
                    )

                availability?.let {

                    openingTime =
                        it.openingTime

                    closingTime =
                        it.closingTime

                    binding.tvOpeningTime.text =
                        openingTime

                    binding.tvClosingTime.text =
                        closingTime

                    binding.switchMaintenance
                        .isChecked =
                        it.maintenanceMode

                    setCheckedDays(
                        it.workingDays
                    )
                }
            }
    }

    private fun setCheckedDays(
        workingDays: List<String>
    ) {

        val dayMap =
            mapOf(

                "Monday" to binding.checkMonday,
                "Tuesday" to binding.checkTuesday,
                "Wednesday" to binding.checkWednesday,
                "Thursday" to binding.checkThursday,
                "Friday" to binding.checkFriday,
                "Saturday" to binding.checkSaturday,
                "Sunday" to binding.checkSunday
            )

        for ((day, checkBox) in dayMap) {

            checkBox.isChecked =
                workingDays.contains(day)
        }
    }
}