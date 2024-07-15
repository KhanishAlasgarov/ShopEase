package com.khanish.shopease.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


data class Product(
    val id: Int = 0,
    val name: String = "",
    val price: Double = 0.0,
    val picUrls: List<String> = emptyList(),
    val categoryId: Int = -1,
    var favorite: Boolean = false
)