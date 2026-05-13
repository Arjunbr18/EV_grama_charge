package com.example.ev_grama_charge.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun registerUser(
        name: String,
        email: String,
        password: String,
        role: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                val userId = auth.currentUser?.uid ?: ""

                val userMap = hashMapOf(
                    "userId" to userId,
                    "name" to name,
                    "email" to email,
                    "role" to role,
                    "fcmToken" to ""
                )

                db.collection("users").document(userId)
                    .set(userMap)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onFailure(it.message ?: "Error") }
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Error")
            }
    }

    fun loginUser(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = auth.currentUser?.uid ?: ""

                db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener { document ->
                        val role = document.getString("role") ?: ""
                        onSuccess(role)
                    }
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Login Failed")
            }
    }
}