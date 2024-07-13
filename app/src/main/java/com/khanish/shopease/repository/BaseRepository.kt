package com.khanish.shopease.repository


import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.khanish.shopease.remote.NetworkResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class BaseRepository(val db: FirebaseFirestore) {

    protected suspend fun <T> safeFirestoreRequest(request: suspend () -> T) = flow {
        try {
            val result = request()
            emit(NetworkResource.Success(result))
        } catch (e: Exception) {
            emit(NetworkResource.Error<T>(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)


    protected suspend fun safeDocumentRequest(request: suspend () -> DocumentReference): Flow<NetworkResource<String>> = flow {
        try {
            val documentReference = request()
            emit(NetworkResource.Success(documentReference.id))
        } catch (e: Exception) {
            emit(NetworkResource.Error<String>(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)




}