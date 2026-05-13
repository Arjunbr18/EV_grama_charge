package com.example.ev_grama_charge.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.onboarding.HostOnboardingActivity
import com.example.ev_grama_charge.ui.host.HostHomeActivity
import com.example.ev_grama_charge.ui.user.UserHomeActivity
import com.example.ev_grama_charge.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        viewModel = AuthViewModel()

        val email =
            findViewById<EditText>(R.id.etEmail)

        val password =
            findViewById<EditText>(R.id.etPassword)

        val loginBtn =
            findViewById<Button>(R.id.btnLogin)

        val goRegister =
            findViewById<TextView>(R.id.tvRegister)

        loginBtn.setOnClickListener {

            viewModel.login(

                email.text.toString(),
                password.text.toString(),

                { role ->

                    Toast.makeText(
                        this,
                        "Login Success",
                        Toast.LENGTH_SHORT
                    ).show()

                    if (role == "USER") {

                        startActivity(
                            Intent(
                                this,
                                UserHomeActivity::class.java
                            )
                        )

                        overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )

                        finish()

                    } else {

                        checkHostProfile()
                    }
                },

                {

                    Toast.makeText(
                        this,
                        it,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }

        goRegister.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )

            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }

    private fun checkHostProfile() {

        val userId =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("hosts")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->

                if (document.exists()) {

                    // HOST PROFILE EXISTS

                    startActivity(
                        Intent(
                            this,
                            HostHomeActivity::class.java
                        )
                    )

                } else {

                    // FIRST TIME HOST

                    startActivity(
                        Intent(
                            this,
                            HostOnboardingActivity::class.java
                        )
                    )
                }

                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )

                finish()
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed To Check Host Profile",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun finish() {

        super.finish()

        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }
}