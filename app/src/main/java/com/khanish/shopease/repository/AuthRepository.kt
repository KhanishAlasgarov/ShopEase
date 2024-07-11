package com.khanish.shopease.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.khanish.shopease.model.AuthResultModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {
    private lateinit var authModel: AuthResultModel

    suspend fun createAccount(email: String, password: String): AuthResultModel {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            AuthResultModel("Registration was successful", true)
        } catch (e: Exception) {
            AuthResultModel(e.localizedMessage?.toString(), false)
        }
    }

    suspend fun loginAccount(email: String, password: String): AuthResultModel {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthResultModel("Login successful", true)
        } catch (e: Exception) {
            AuthResultModel(e.localizedMessage?.toString(), false)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun logout() {
        firebaseAuth.signOut()
    }

}