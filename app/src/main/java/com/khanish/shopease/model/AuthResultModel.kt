package com.khanish.shopease.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthResultModel(
    val message: String? = null,
    val isSuccess: Boolean,
): Parcelable
