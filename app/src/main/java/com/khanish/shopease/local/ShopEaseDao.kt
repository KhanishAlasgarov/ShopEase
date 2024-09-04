package com.khanish.shopease.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khanish.shopease.model.BasketProductEntity
import com.khanish.shopease.model.Product
import com.khanish.shopease.model.ProductEntity
import com.khanish.shopease.model.RecentSearchItem
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

    @Query("Select * from basket_product where productId=:id and size=:size")
    suspend fun getBasketItem(id: Int, size: String): BasketProductEntity?

    @Query("Select * from basket_product where id=:id")
    suspend fun getBasketItemById(id: Int): BasketProductEntity?

    @Delete
    suspend fun deleteProductFromBasket(basketProductEntity: BasketProductEntity)

    @Query("Delete from basket_product")
    suspend fun clearBasket()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSearchItem(searchItem: RecentSearchItem)

    @Query("Select * from recent_search where text=:text")
    suspend fun getRecentItemByText(text: String): RecentSearchItem?

    @Query("Select * from recent_search")
    suspend fun getAllRecentSearchItems(): List<RecentSearchItem>

    @Delete
    suspend fun deleteSearchItem(searchItem: RecentSearchItem)

    @Query("Select * from recent_search where id=:id")
    suspend fun getSearchItemById(id: Int): RecentSearchItem?

    @Query("DELETE FROM recent_search")
    suspend fun deleteAll()


}