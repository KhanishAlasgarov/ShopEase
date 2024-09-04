package com.khanish.shopease.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.khanish.shopease.model.Product
import com.khanish.shopease.remote.NetworkResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepository @Inject constructor(db: FirebaseFirestore) : BaseRepository(db) {
    private val productCollection: CollectionReference = db.collection("Products")

    suspend fun getAllProducts(): Flow<NetworkResource<List<Product>>> = safeFirestoreRequest {
        val querySnapshot = productCollection.get().await()
        val products = querySnapshot.documents.mapNotNull { it.toObject(Product::class.java) }
        products
    }


    suspend fun getProductById(id: Int): Flow<NetworkResource<Product?>> = safeFirestoreRequest {
        val documentSnapshot = productCollection.document(id.toString()).get().await()
        documentSnapshot.toObject(Product::class.java)
    }

    suspend fun getProductsByCategoryId(categoryId: Int): Flow<NetworkResource<List<Product>>> =
        safeFirestoreRequest {
            val querySnapshot =
                productCollection.whereEqualTo("categoryId", categoryId).get().await()
            val products = querySnapshot.documents.mapNotNull { it.toObject(Product::class.java) }
            products
        }
}