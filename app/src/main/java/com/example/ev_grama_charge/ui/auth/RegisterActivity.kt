package com.example.ev_grama_charge.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.viewmodel.AuthViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        viewModel = AuthViewModel()

        val etName =
            findViewById<TextInputEditText>(
                R.id.etName
            )

        val etEmail =
            findViewById<TextInputEditText>(
                R.id.etEmail
            )

        val etPassword =
            findViewById<TextInputEditText>(
                R.id.etPassword
            )

        val etConfirmPassword =
            findViewById<TextInputEditText>(
                R.id.etConfirmPassword
            )

        val btnRegister =
            findViewById<MaterialButton>(
                R.id.btnRegister
            )

        val radioGroupRole =
            findViewById<RadioGroup>(
                R.id.radioGroupRole
            )

        val tvLogin =
            findViewById<android.widget.TextView>(
                R.id.tvLogin
            )

        btnRegister.setOnClickListener {

            val name =
                etName.text.toString().trim()

            val email =
                etEmail.text.toString().trim()

            val password =
                etPassword.text.toString().trim()

            val confirmPassword =
                etConfirmPassword.text
                    .toString()
                    .trim()

            if (
                name.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                confirmPassword.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (password != confirmPassword) {

                Toast.makeText(
                    this,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val selectedRoleId =
                radioGroupRole.checkedRadioButtonId

            val selectedRoleButton =
                findViewById<RadioButton>(
                    selectedRoleId
                )

            val selectedRole =
                if (
                    selectedRoleButton.text
                        .toString() == "Host"
                ) {

                    "HOST"

                } else {

                    "USER"
                }

            viewModel.register(

                name,
                email,
                password,
                selectedRole,

                {

                    Toast.makeText(
                        this,
                        "Registered Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(
                        Intent(
                            this,
                            LoginActivity::class.java
                        )
                    )

                    overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                    )

                    finish()
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

        tvLogin.setOnClickListener {

            finish()
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