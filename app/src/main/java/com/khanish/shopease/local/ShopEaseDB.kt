package com.khanish.shopease.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.khanish.shopease.model.Product

@Database(entities = [Product::class], version = 1)
abstract class ShopEaseDB : RoomDatabase() {

    abstract fun shopEaseDao(): ShopEaseDao
}