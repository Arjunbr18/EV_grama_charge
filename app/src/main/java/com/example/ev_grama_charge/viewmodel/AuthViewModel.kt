package com.example.ev_grama_charge.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ev_grama_charge.data.repository.AuthRepository

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    fun register(
        name: String,
        email: String,
        password: String,
        role: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        repo.registerUser(name, email, password, role, onSuccess, onFailure)
    }

    fun login(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        repo.loginUser(email, password, onSuccess, onFailure)
    }
}