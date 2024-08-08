package com.khanish.shopease.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khanish.shopease.databinding.SearchItemBinding
import com.khanish.shopease.model.Product

class SearchItemAdapter : RecyclerView.Adapter<SearchItemAdapter.ViewHolder>() {

    private val products = arrayListOf<Product>()

    lateinit var navigateDetail: (id: Int) -> Unit

    inner class ViewHolder(val searchItemBinding: SearchItemBinding) :
        RecyclerView.ViewHolder(searchItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = products[position]

        holder.searchItemBinding.product = data

        holder.searchItemBinding.root.setOnClickListener {
            navigateDetail(data.id)
        }

    }

    fun updateList(list: List<Product>) {
        products.clear()
        products.addAll(list)
        notifyDataSetChanged()
    }

}