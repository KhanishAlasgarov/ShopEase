package com.khanish.shopease.ui.myDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.khanish.shopease.model.AuthModel
import com.khanish.shopease.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyDetailViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    private val _user = MutableLiveData<AuthModel>()
    val user: MutableLiveData<AuthModel> get() = _user


    fun updateUserValue(fullName: String, phoneNumber: String, email: String, date: String) {
        viewModelScope.launch(Dispatchers.IO)
        {
            val db = FirebaseFirestore.getInstance()
            val documentRef = db.collection("Users")
                .document(authRepository.getCurrentUser()?.uid!!)

            documentRef.update("fullName", fullName).await()
            documentRef.update("phoneNumber", phoneNumber).await()
            documentRef.update("email", email).await()
            documentRef.update("dateOfBirth", date).await()

        }
    }

    fun getUserById() {
        viewModelScope.launch(Dispatchers.IO) {
            val data =
                authRepository.getUserById(authRepository.getCurrentUser()?.uid!!)

            withContext(Dispatchers.Main) {
                data?.let {
                    _user.value = it
                }
            }


        }
    }
}