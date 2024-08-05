package com.khanish.shopease.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.khanish.shopease.model.BasketProductEntity

import com.khanish.shopease.model.ProductEntity

@Database(entities = [ProductEntity::class, BasketProductEntity::class], version = 1)
abstract class ShopEaseDB : RoomDatabase() {

    abstract fun shopEaseDao(): ShopEaseDao
}