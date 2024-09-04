package com.khanish.shopease.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthResultModel(
    val id: String?=null,
    val message: String? = null,
    val isSuccess: Boolean,
) : Parcelable
