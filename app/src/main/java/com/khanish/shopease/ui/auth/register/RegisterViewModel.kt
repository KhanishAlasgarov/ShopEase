package com.khanish.shopease.ui.auth.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khanish.shopease.model.AuthResultModel
import com.khanish.shopease.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {


    val authModel = MutableLiveData<AuthResultModel?>()

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading

    fun signUp(email: String, password: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.createAccount(email, password)

            withContext(Dispatchers.Main) {
                authModel.value = result
                _loading.value = false
            }
        }
    }
}