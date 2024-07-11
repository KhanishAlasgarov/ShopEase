package com.khanish.shopease.ui.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khanish.shopease.model.AuthResultModel
import com.khanish.shopease.repository.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _authModel = MutableLiveData<AuthResultModel>()
    val authModel: MutableLiveData<AuthResultModel> get() = _authModel

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading

    fun signIn(email: String, password: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val authModel = authRepository.loginAccount(email, password)

            withContext(Dispatchers.Main) {
                _authModel.value = authModel
                _loading.value = false
            }
        }

    }


}