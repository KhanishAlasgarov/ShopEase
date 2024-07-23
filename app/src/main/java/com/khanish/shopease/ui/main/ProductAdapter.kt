package com.khanish.shopease.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khanish.shopease.databinding.ProductItemBinding
import com.khanish.shopease.model.Product

class ProductAdapter :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private val products = arrayListOf<Product>()
    lateinit var addToFavorite: (product: Product, callBack: (Boolean) -> Unit) -> Unit
    lateinit var navigateToDetail: (id: Int) -> Unit

    inner

    class ViewHolder(val productItemBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(productItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = products[position]
        holder.productItemBinding.product = data

        holder.productItemBinding.addToFavorite.setOnClickListener {

            fun changeItemFavoriteProperty(isFavorite: Boolean) {
                data.favorite = isFavorite
                notifyItemChanged(position)
            }

            addToFavorite(data, ::changeItemFavoriteProperty)
        }

        holder.productItemBinding.root.setOnClickListener {
            navigateToDetail(data.id)
        }


    }


    fun updateList(list: List<Product>) {
        products.clear()
        products.addAll(list)
        notifyDataSetChanged()
    }
}