package com.khanish.shopease.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khanish.shopease.model.BasketProductEntity
import com.khanish.shopease.model.Product
import com.khanish.shopease.model.ProductEntity
import com.khanish.shopease.model.Size

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


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProductToBasket(vararg product: BasketProductEntity): List<Long>

    @Query("Select * from basket_product")
    suspend fun getBasket(): List<BasketProductEntity>

    @Query("Select * from basket_product where id=:id and size=:size")
    suspend fun getBasketItem(id: Int, size: String): BasketProductEntity?

}