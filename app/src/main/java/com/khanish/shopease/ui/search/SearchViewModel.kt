package com.khanish.shopease.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khanish.shopease.local.ShopEaseDao
import com.khanish.shopease.model.Product
import com.khanish.shopease.model.RecentSearchItem
import com.khanish.shopease.remote.NetworkResource
import com.khanish.shopease.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val db: ShopEaseDao,
    private val productRepository: ProductRepository
) : ViewModel() {


    private val _products = MutableLiveData<List<Product>>()
    val products: MutableLiveData<List<Product>> get() = _products

    private val _error = MutableLiveData<String>()
    val error: MutableLiveData<String> get() = _error

    private val _recentProducts = MutableLiveData<List<RecentSearchItem>>()
    val recentProducts: MutableLiveData<List<RecentSearchItem>> get() = _recentProducts

    fun setSearchItem(text: String) {
        viewModelScope.launch(Dispatchers.IO)
        {
            val data = db.getRecentItemByText(text)

            if (data == null) {
                db.addSearchItem(RecentSearchItem(id = 0, text = text))

            } else {
                db.deleteSearchItem(data)
                db.addSearchItem(RecentSearchItem(id = 0, text = text))
            }
            getAllRecentItems()
        }

    }

    fun removeSearchItem(id: Int, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO)
        {
            val data = db.getSearchItemById(id)

            data?.let {
                db.deleteSearchItem(data)

                withContext(Dispatchers.Main) {
                    callback()
                }
            }
        }
    }

    fun getAllRecentItems() {
        viewModelScope.launch(Dispatchers.IO)
        {
            val data = db.getAllRecentSearchItems()

            withContext(Dispatchers.Main) {
                _recentProducts.value = data

            }
        }
    }

    fun fetchProducts() {
        viewModelScope.launch {
            val favoriteProductIds = db.getAllProduct().map { it.id }.toSet()
            val response = productRepository.getAllProducts()

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
    }

    fun clearAllRecentItem() {
        viewModelScope.launch(Dispatchers.IO) {
            db.deleteAll()
            getAllRecentItems()

        }
    }

}