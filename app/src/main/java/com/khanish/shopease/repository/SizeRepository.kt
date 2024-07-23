package com.khanish.shopease.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.khanish.shopease.model.Size
import com.khanish.shopease.remote.NetworkResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SizeRepository @Inject constructor(db: FirebaseFirestore) : BaseRepository(db) {
    private val sizeCollection: CollectionReference = db.collection("Sizes")
    suspend fun getAllSizes(): Flow<NetworkResource<List<Size>>> = safeFirestoreRequest {
        val querySnapshot = sizeCollection.get().await()
        val sizes = querySnapshot.documents.mapNotNull { it.toObject(Size::class.java) }
        sizes
    }
}