package com.khanish.shopease.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("basket_product")
data class BasketProductEntity(
    @PrimaryKey
    val id: Int,
    val count: Int,
    val size: String
)