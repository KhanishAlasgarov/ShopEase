package com.khanish.shopease.ui.splash

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.khanish.shopease.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    fun getUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }

}