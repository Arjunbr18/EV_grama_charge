package com.example.ev_grama_charge.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            // FORWARD TRANSITION
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )

            finish()

        }, 5000)
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