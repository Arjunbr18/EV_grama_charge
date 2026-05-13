package com.example.ev_grama_charge.ui.user

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.R

class CalculatorActivity : AppCompatActivity() {

    private lateinit var etBattery: EditText
    private lateinit var etTime: EditText
    private lateinit var tvEnergy: TextView
    private lateinit var tvDistance: TextView
    private lateinit var btnCalculate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        etBattery = findViewById(R.id.etBattery)
        etTime = findViewById(R.id.etTime)
        tvEnergy = findViewById(R.id.tvEnergy)
        tvDistance = findViewById(R.id.tvDistance)
        btnCalculate = findViewById(R.id.btnCalculate)

        btnCalculate.setOnClickListener {
            calculate()
        }
    }

    private fun calculate() {
        val battery = etBattery.text.toString().toDoubleOrNull()
        val time = etTime.text.toString().toDoubleOrNull()

        if (battery == null || time == null) {
            Toast.makeText(this, "Enter valid inputs", Toast.LENGTH_SHORT).show()
            return
        }

        // Simple logic:
        val chargingRate = battery / 4   // full charge in 4 hrs assumption
        val energy = (time / 60) * chargingRate

        val distance = energy * 8   // 1 kWh ≈ 8 km (approx)

        tvEnergy.text = "Energy: %.2f kWh".format(energy)
        tvDistance.text = "Distance: %.2f km".format(distance)
    }
}