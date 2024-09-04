package com.khanish.shopease.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.khanish.shopease.model.AuthModel
import com.khanish.shopease.model.AuthResultModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {
    private lateinit var authModel: AuthResultModel

    suspend fun createAccount(
        email: String,
        password: String,
        callback: (id:String) -> Unit
    ): AuthResultModel {
        return try {
            val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            callback(user.user?.uid!!)
            AuthResultModel(id = user.user?.uid, "Registration was successful", true)
        } catch (e: Exception) {
            AuthResultModel(message = e.localizedMessage?.toString(), isSuccess = false)
        }
    }

    suspend fun loginAccount(email: String, password: String): AuthResultModel {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthResultModel(message = "Login successful", isSuccess = true)
        } catch (e: Exception) {
            AuthResultModel(message = e.localizedMessage?.toString(), isSuccess = false)
        }
    }

    suspend fun getUserById(userId: String):AuthModel? {
        val querySnapshot = FirebaseFirestore.getInstance().collection("Users")
            .whereEqualTo("id", userId)
            .get()
            .await()
        val data = querySnapshot.documents[0].toObject(AuthModel::class.java)
        return data


    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun logout() {
        firebaseAuth.signOut()
    }

}