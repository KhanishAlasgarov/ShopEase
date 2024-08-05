package com.khanish.shopease.ui.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khanish.shopease.local.ShopEaseDao
import com.khanish.shopease.model.BasketProductEntity
import com.khanish.shopease.model.Product
import com.khanish.shopease.model.ProductEntity
import com.khanish.shopease.model.Size
import com.khanish.shopease.remote.NetworkResource
import com.khanish.shopease.repository.ProductRepository
import com.khanish.shopease.repository.SizeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val sizeRepository: SizeRepository,
    private val db: ShopEaseDao
) : ViewModel() {

    private val _favoriteValue = MutableLiveData<Boolean>()
    val favoriteValue: MutableLiveData<Boolean> get() = _favoriteValue
    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading

    private val _product = MutableLiveData<Product?>()
    val product: MutableLiveData<Product?> get() = _product

    private val _sizes = MutableLiveData<List<Size>>()
    val sizes: MutableLiveData<List<Size>> get() = _sizes

    private val _error = MutableLiveData<String>()
    val error: MutableLiveData<String> get() = _error

    private val _basketLoading = MutableLiveData<Boolean>()
    val basketLoading: MutableLiveData<Boolean> get() = _basketLoading

    fun addToFavorite() {

        viewModelScope.launch(Dispatchers.IO) {
            _product.value?.let {
                val data = db.getProductById(it.id)
                val favorite: Boolean
                if (data == null) {
                    db.addProduct(ProductEntity(it.id))
                    favorite = true
                } else {
                    db.deleteProduct(data)
                    favorite = false
                }

                withContext(Dispatchers.Main) {
                    _favoriteValue.value = favorite
                }
            }

        }
    }

    fun addToBasket(id: Int, size: Size) {
        viewModelScope.launch(Dispatchers.IO) {

            val basketItem = db.getBasketItem(id, size.size)

            if (basketItem == null) {
                db.addProductToBasket(
                    BasketProductEntity(
                        id, 1, size.size
                    )
                )
            } else {
                val count = basketItem.count + 1
                db.addProductToBasket(BasketProductEntity(id, count, size.size))
            }

        }
    }

    fun getAllSize() {
        viewModelScope.launch {
            sizeRepository.getAllSizes().collectLatest { response ->
                when (response) {
                    is NetworkResource.Success -> {
                        response.data?.let {
                            _sizes.value = it
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
    }

    private suspend fun checkIsFavorite(product: Product): Boolean {
        val productsIds = db.getAllProduct().map { it.id }
        return product.id in productsIds
    }

    fun getProductById(id: Int) {
        _loading.value = true
        viewModelScope.launch {
            productRepository.getProductById(id).collectLatest { response ->

                when (response) {
                    is NetworkResource.Success -> {
                        response.data?.let {
                            it.favorite = checkIsFavorite(it)
                            _product.value = it
                        }
                    }

                    is NetworkResource.Error -> {
                        response.message?.let {
                            _error.value = it
                        }
                    }
                }
                _loading.value = false

            }
        }

    }


}