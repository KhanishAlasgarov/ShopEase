package com.khanish.shopease.ui.main

import android.util.Log

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase

import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.getValue
import com.google.firebase.firestore.firestore
import com.khanish.shopease.local.ShopEaseDao
import com.khanish.shopease.model.Category
import com.khanish.shopease.model.Product
import com.khanish.shopease.model.ProductEntity
import com.khanish.shopease.remote.NetworkResource
import com.khanish.shopease.repository.CategoryRepository
import com.khanish.shopease.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Collections
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CategoryRepository,
    private val productRepository: ProductRepository,
    private val db: ShopEaseDao
) :
    ViewModel() {

    val minValue = MutableLiveData<Int>()
    val maxValue = MutableLiveData<Int>()

    private val _categories = MutableLiveData<List<Category>>()
    val categories: MutableLiveData<List<Category>> get() = _categories

    private val _products = MutableLiveData<List<Product>>()
    val products: MutableLiveData<List<Product>> get() = _products

    private val _productLoading = MutableLiveData<Boolean>()
    val productLoading: MutableLiveData<Boolean> get() = _productLoading

    val filteredData = MutableLiveData<List<Product>>()
    val rangeSliderValues = MutableLiveData<List<Float>>()
    val searchedProducts = MutableLiveData<List<Product>?>()


    private val _error = MutableLiveData<String>()
    val error: MutableLiveData<String> get() = _error


    fun fetchData(categoryId: Int?) {

        _productLoading.value = true
        viewModelScope.launch {
            fetchCategories()
            fetchProducts(categoryId)
            _productLoading.value = false
        }
    }

    private suspend fun fetchCategories() {

        repository.getAllCategories().collectLatest { response ->
            when (response) {
                is NetworkResource.Success -> {
                    response.data?.let {

                        _categories.value = it
                    }
                }

                is NetworkResource.Error -> {
                    response.message?.let {
                        _error.value = it
                    }
                }
            }
        }
    }


    private suspend fun fetchProducts(categoryId: Int?) {

        val favoriteProductIds = db.getAllProduct().map { it.id }.toSet()
        val response = if (categoryId == null || categoryId == 0) {
            productRepository.getAllProducts()
        } else {
            productRepository.getProductsByCategoryId(categoryId)
        }

        response.collectLatest { networkResource ->
            when (networkResource) {
                is NetworkResource.Success -> {
                    networkResource.data?.let {
                        it.forEach { product ->
                            product.favorite = product.id in favoriteProductIds
                        }
                        _products.value = it
                    }
                }

                is NetworkResource.Error -> {
                    networkResource.message?.let {
                        _error.value = it
                    }
                }
            }
        }

    }

    fun addProductToDb(product: Product, callback: (Boolean) -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            val data = db.getProductById(product.id)
            val favorite: Boolean
            if (data == null) {
                db.addProduct(ProductEntity(product.id))
                favorite = true
            } else {
                db.deleteProduct(data)
                favorite = false
            }
            withContext(Dispatchers.Main) {
                callback(favorite)
            }
        }
    }


//    fun fetchProductById(productId: String) {
//        loading.value = true
//        viewModelScope.launch {
//            repository.getProductById(productId).collectLatest { response ->
//                when (response) {
//                    is NetworkResource.Success -> {
//                        product.value = response.data
//                    }
//                    is NetworkResource.Error -> {
//                        response.message?.let {
//                            error.value = it
//                        }
//                    }
//                }
//                loading.value = false
//            }
//        }
//    }


//    fun addProduct(newProduct: Product) {
//        loading.value = true
//        viewModelScope.launch {
//            repository.addProduct(newProduct).collectLatest { response ->
//                when (response) {
//                    is NetworkResource.Success -> {
//                        addedProductId.value = response.data
//                    }
//                    is NetworkResource.Error -> {
//                        response.message?.let {
//                            error.value = it
//                        }
//                    }
//                }
//                loading.value = false
//            }
//        }
//    }


//    fun setProduct(productId: String, updatedProduct: Product) {
//        loading.value = true
//        viewModelScope.launch {
//            repository.setProduct(productId, updatedProduct).collectLatest { response ->
//                when (response) {
//                    is NetworkResource.Success -> {
//                        // Optionally handle success
//                    }
//                    is NetworkResource.Error -> {
//                        response.message?.let {
//                            error.value = it
//                        }
//                    }
//                }
//                loading.value = false
//            }
//        }
//    }


}