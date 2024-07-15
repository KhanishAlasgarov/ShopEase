package com.khanish.shopease.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.khanish.shopease.model.Product
import com.khanish.shopease.model.ProductEntity

@Dao
interface ShopEaseDao {
    @Insert
    suspend fun addProduct(vararg product: ProductEntity): List<Long>

    @Query("Select * from product_table")
    suspend fun getAllProduct(): List<ProductEntity>

    @Query("Select * from product_table where id=:id")
    suspend fun getProductById(id: Int): ProductEntity?

    @Delete
    suspend fun deleteProduct(product: ProductEntity)
}