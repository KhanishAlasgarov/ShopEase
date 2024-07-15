package com.khanish.shopease.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity("product_table")
data class ProductEntity(
    @PrimaryKey
    val id: Int
)