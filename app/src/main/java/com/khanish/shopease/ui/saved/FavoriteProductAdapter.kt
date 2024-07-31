package com.khanish.shopease.ui.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khanish.shopease.databinding.ProductItemFavoriteBinding
import com.khanish.shopease.model.Product

class FavoriteProductAdapter : RecyclerView.Adapter<FavoriteProductAdapter.ViewHolder>() {

    private val products = arrayListOf<Product>()
    lateinit var navigateToDetail: (id: Int) -> Unit
    lateinit var addToFavorite: (product: Product) -> Unit
    lateinit var checkCount: (products: ArrayList<Product>) -> Unit

    inner class ViewHolder(val productItemFavoriteBinding: ProductItemFavoriteBinding) :
        RecyclerView.ViewHolder(productItemFavoriteBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ProductItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = products[position]
        holder.productItemFavoriteBinding.product = data

        holder.productItemFavoriteBinding.root.setOnClickListener {
            navigateToDetail(data.id)
        }

        holder.productItemFavoriteBinding.addToFavorite.setOnClickListener {
            addToFavorite(data)
            products.remove(data)
            notifyItemRemoved(position)
            checkCount(products)

        }

    }

    fun updateList(list: List<Product>) {
        products.clear()
        products.addAll(list)
        notifyDataSetChanged()
    }
}