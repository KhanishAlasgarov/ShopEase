package com.khanish.shopease.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.khanish.shopease.model.Product

@Dao
interface ShopEaseDao {
    @Insert
    suspend fun addProduct(vararg product: Product): List<Long>

    @Query("Select * from product_table")
    suspend fun getAllProduct(): List<Product>

    @Delete
    suspend fun deleteProduct(product: Product)
}