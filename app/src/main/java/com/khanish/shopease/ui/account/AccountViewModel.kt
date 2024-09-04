package com.khanish.shopease.ui.account

import androidx.lifecycle.ViewModel
import com.khanish.shopease.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {


        fun logout(){
            authRepository.logout()
        }
}