package com.khanish.shopease.ui.cart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khanish.shopease.local.ShopEaseDao
import com.khanish.shopease.model.BasketUiModel
import com.khanish.shopease.remote.NetworkResource
import com.khanish.shopease.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val db: ShopEaseDao,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading

    private val _products = MutableLiveData<List<BasketUiModel>>()
    val products: MutableLiveData<List<BasketUiModel>> get() = _products

    fun removeProduct(id: Int, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO)
        {
            val product = db.getBasketItemById(id)
            product?.let {
                db.deleteProductFromBasket(product)
                withContext(Dispatchers.Main) {
                    callback()
                }
            }
        }

    }


    fun getBasketItems() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _loading.value = true
            }
            val response = productRepository.getAllProducts()
            val basketProducts = db.getBasket()


            response.collectLatest { networkResource ->
                when (networkResource) {
                    is NetworkResource.Success -> {
                        networkResource.data?.let { products ->
                            val data = arrayListOf<BasketUiModel>()
                            products.forEach { product ->


                                basketProducts.forEach { basketProduct ->
                                    if (product.id == basketProduct.productId) {
                                        data.add(
                                            BasketUiModel(
                                                basketProduct.id,
                                                basketProduct.productId,
                                                product.name,
                                                product.picUrls[0],
                                                basketProduct.size,
                                                product.price,
                                                basketProduct.count
                                            )
                                        )
                                    }
                                }

                            }
                            withContext(Dispatchers.Main) {
                                _products.value = data
                            }
                        }
                    }

                    is NetworkResource.Error -> {

                    }
                }

                withContext(Dispatchers.Main) {
                    _loading.value = false
                }

            }

        }

    }

    fun decreaseProduct(id: Int, callback: (isDeleted: Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = db.getBasketItemById(id)
            data?.let {
                val isDeleted: Boolean
                if (data.count != 1) {
                    isDeleted = false
                    data.count--
                    db.addProductToBasket(data)
                } else {
                    isDeleted = true
                    db.deleteProductFromBasket(data)

                }

                withContext(Dispatchers.Main) {
                    callback(isDeleted)
                }
            }
        }
    }

    fun increaseProduct(id: Int, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = db.getBasketItemById(id)

            data?.let {
                data.count++
                db.addProductToBasket(data)

                withContext(Dispatchers.Main) {
                    callback()
                }
            }
        }
    }


}