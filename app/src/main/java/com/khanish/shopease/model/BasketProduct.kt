package com.khanish.shopease.model


data class BasketProduct(
    val productId: Int=0,
    var orderId: String="",
    var count: Int=0,
    val size: String=""
)