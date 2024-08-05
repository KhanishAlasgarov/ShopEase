package com.khanish.shopease.model

data class BasketUiModel(
    val id: Int,
    val productId:Int,
    val name:String,
    val imageUrl: String,
    val size: String,
    val price: Double,
    var count: Int
) {
}