package com.khanish.shopease.ui.myOrders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khanish.shopease.databinding.OrderItemBinding
import com.khanish.shopease.model.OrderedProduct
import com.khanish.shopease.model.Product

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    inner class ViewHolder(val orderItemBinding: OrderItemBinding) :
        RecyclerView.ViewHolder(orderItemBinding.root)

    private val orders = arrayListOf<OrderedProduct>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = OrderItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = orders[position]
        holder.orderItemBinding.product = data
    }

    fun updateList(list: List<OrderedProduct>) {
        orders.clear()
        orders.addAll(list)
        notifyDataSetChanged()
    }
}