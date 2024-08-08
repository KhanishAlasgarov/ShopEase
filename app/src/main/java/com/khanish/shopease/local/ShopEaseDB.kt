package com.khanish.shopease.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.khanish.shopease.model.BasketProductEntity

import com.khanish.shopease.model.ProductEntity
import com.khanish.shopease.model.RecentSearchItem

@Database(
    entities = [ProductEntity::class, BasketProductEntity::class, RecentSearchItem::class],
    version = 4
)
abstract class ShopEaseDB : RoomDatabase() {

    abstract fun shopEaseDao(): ShopEaseDao
}