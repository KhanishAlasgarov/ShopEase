package com.khanish.shopease.model

import java.time.LocalDateTime

data class OrderModel(val total: Double, val datetime: LocalDateTime, var userId: String = "")