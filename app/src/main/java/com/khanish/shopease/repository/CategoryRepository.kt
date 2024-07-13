package com.khanish.shopease.repository


import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.khanish.shopease.model.Category
import com.khanish.shopease.remote.NetworkResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class CategoryRepository @Inject constructor(db: FirebaseFirestore) :
    BaseRepository(db) {

    private val categoriesCollection: CollectionReference = db.collection("Categories")


    suspend fun getAllCategories(): Flow<NetworkResource<List<Category>>> = safeFirestoreRequest {
        val querySnapshot = categoriesCollection.get().await()
        val categories = querySnapshot.documents.mapNotNull { it.toObject(Category::class.java) }
        categories
    }


    suspend fun getCategoryById(productId: String): Flow<NetworkResource<Category?>> =
        safeFirestoreRequest {
            val documentSnapshot = categoriesCollection.document(productId).get().await()
            documentSnapshot.toObject(Category::class.java)
        }


    suspend fun addCategory(category: Category): Flow<NetworkResource<String>> =
        safeDocumentRequest {
            categoriesCollection.add(category).await()
        }


    suspend fun setCategory(productId: String, product: Category): Flow<NetworkResource<Void>> =
        safeFirestoreRequest {
            categoriesCollection.document(productId).set(product).await()
        }
}