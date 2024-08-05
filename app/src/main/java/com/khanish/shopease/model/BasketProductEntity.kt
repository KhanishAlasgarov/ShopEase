package com.khanish.shopease.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("basket_product")
data class BasketProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val productId: Int,
    var count: Int,
    val size: String
)