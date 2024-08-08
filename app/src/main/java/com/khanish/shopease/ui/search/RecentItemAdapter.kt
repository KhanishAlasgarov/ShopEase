package com.khanish.shopease.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khanish.shopease.databinding.RecentItemBinding
import com.khanish.shopease.model.Product
import com.khanish.shopease.model.RecentSearchItem

class RecentItemAdapter : RecyclerView.Adapter<RecentItemAdapter.ViewHolder>() {

    private val products = arrayListOf<RecentSearchItem>()
    lateinit var deleteRecentItem: (id: Int, () -> Unit) -> Unit
    lateinit var searchByRecentItem: (value:String) -> Unit

    inner class ViewHolder(val recentItemBinding: RecentItemBinding) :
        RecyclerView.ViewHolder(recentItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RecentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = products[position]
        holder.recentItemBinding.searchItemTitle.text = data.text

        holder.recentItemBinding.recentItemDeleteButton.setOnClickListener {
            deleteRecentItem(data.id) {
                products.remove(data)
                notifyItemRemoved(holder.bindingAdapterPosition)
            }

        }

        holder.recentItemBinding.root.setOnClickListener {
            searchByRecentItem(data.text)
        }
    }

    fun updateList(list: List<RecentSearchItem>) {
        products.clear()
        products.addAll(list)
        notifyDataSetChanged()
    }
}