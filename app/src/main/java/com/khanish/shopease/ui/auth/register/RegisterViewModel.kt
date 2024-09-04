package com.khanish.shopease.ui.auth.register

import android.os.health.TimerStat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.khanish.shopease.model.AuthResultModel
import com.khanish.shopease.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {


    val authModel = MutableLiveData<AuthResultModel?>()

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading


    fun signUp(email: String, password: String, fullName: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.createAccount(email, password){ id->
                postSignUpData(email, fullName,id)
            }

            withContext(Dispatchers.Main) {
                authModel.value = result
                _loading.value = false
            }

        }
    }

    private fun postSignUpData(email: String, fullName: String,id:String) {

        FirebaseFirestore.getInstance().collection("Users").add(
            hashMapOf(
                "id" to id,
                "fullName" to fullName,
                "email" to email,
                "phoneNumber" to "",
                "dateOfBirth" to LocalDateTime.now()

            )
        )
    }
}