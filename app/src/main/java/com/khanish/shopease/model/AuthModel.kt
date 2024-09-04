package com.khanish.shopease.model

import com.google.firebase.Timestamp

data class AuthModel(
    val id: String = "",
    var phoneNumber: String? = "",
    var fullName: String = "",
    var email: String = "",
    var dateOfBirth: String = ""
)