package com.khanish.shopease.ui.saved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khanish.shopease.local.ShopEaseDao
import com.khanish.shopease.model.Product
import com.khanish.shopease.model.ProductEntity
import com.khanish.shopease.remote.NetworkResource
import com.khanish.shopease.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val db: ShopEaseDao,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: MutableLiveData<List<Product>> get() = _products

    private val _error = MutableLiveData<String>()
    val error: MutableLiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> = _loading

    fun fetchProducts() {
        viewModelScope.launch {
            _loading.value = true
            val favoriteProductIds = db.getAllProduct().map { it.id }.toSet()
            val response = productRepository.getAllProducts()

            response.collectLatest { networkResource ->
                when (networkResource) {
                    is NetworkResource.Success -> {
                        networkResource.data?.let {
                            val data = arrayListOf<Product>()
                            it.forEach { product ->
                                if (product.id in favoriteProductIds) {
                                    product.favorite = true
                                    data.add(product)
                                }
                            }
                            _products.value = data
                        }
                    }

                    is NetworkResource.Error -> {
                        networkResource.message?.let {
                            _error.value = it
                        }
                    }
                }
                _loading.value = false
            }
        }
    }

    fun addProductToDb(product: Product) {

        viewModelScope.launch(Dispatchers.IO) {
            val data = db.getProductById(product.id)

            data?.let {
                db.deleteProduct(data)
            }
        }
    }
}