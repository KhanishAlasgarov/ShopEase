package com.khanish.shopease.ui.myOrders

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.khanish.shopease.model.BasketProduct
import com.khanish.shopease.model.OrderModel
import com.khanish.shopease.model.OrderModel2
import com.khanish.shopease.model.OrderedProduct
import com.khanish.shopease.model.Product
import com.khanish.shopease.repository.AuthRepository
import com.khanish.shopease.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyOrderViewModel @Inject constructor(
    private val authRepository: AuthRepository
) :
    ViewModel() {

    private val _products = MutableLiveData<List<OrderedProduct>>()
    val products: MutableLiveData<List<OrderedProduct>> get() = _products

    val db = FirebaseFirestore.getInstance()
    private val orderRef = db.collection("Orders")
    private val orderedProductsRef = db.collection("OrderedProducts")

    fun getOrders() {

        viewModelScope.launch(Dispatchers.IO) {
            val querySnapshot =
                orderRef.whereEqualTo("userId", authRepository.getCurrentUser()?.uid!!).get()
                    .await()

            val orders = arrayListOf<OrderModel2>()
            for (query in querySnapshot.documents) {
                var data = query.toObject(OrderModel2::class.java)
                data?.orderId = query.id

                data?.let {
                    orders.add(data)
                }
            }
            val orderedProducts = arrayListOf<OrderedProduct>()
            for (order in orders) {
                val orderedProductQuerySnapshot =
                    orderedProductsRef.whereEqualTo(
                        "orderId",
                        order.orderId
                    )
                        .get()
                        .await()

                for (orderedProductsQuery in orderedProductQuerySnapshot.documents) {
                    val orderedProduct = orderedProductsQuery.toObject(BasketProduct::class.java)
                    val product =
                        db.collection("Products").whereEqualTo("id", orderedProduct?.productId)
                            .get()
                            .await().documents[0].toObject(Product::class.java)

                    orderedProducts.add(
                        OrderedProduct(
                            product?.name!!,
                            orderedProduct?.size!!,
                            product?.price.toString()!!,
                            product.picUrls[0]
                        )
                    )
                }


            }

            withContext(Dispatchers.Main) {
                _products.value = orderedProducts
            }
        }
    }
}