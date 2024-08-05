package com.khanish.shopease.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khanish.shopease.databinding.CartItemBinding
import com.khanish.shopease.model.BasketUiModel
import com.khanish.shopease.model.Product

class BasketProductAdapter : RecyclerView.Adapter<BasketProductAdapter.ViewHolder>() {

    private val products = arrayListOf<BasketUiModel>()

    lateinit var removeProduct: (id: Int, callback: () -> Unit) -> Unit
    lateinit var deacreseProduct: (id: Int, callback: (isDeleted: Boolean) -> Unit) -> Unit
    lateinit var increaseProduct: (id: Int, callback: () -> Unit) -> Unit

    inner class ViewHolder(val cartItemBinding: CartItemBinding) :
        RecyclerView.ViewHolder(cartItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = products[position]
        holder.cartItemBinding.product = data

        holder.cartItemBinding.decreaseCount.setOnClickListener {
            deacreseProduct(data.id) { isDeleted ->
                if (isDeleted) {
                    products.remove(data)
                    notifyItemRemoved(holder.bindingAdapterPosition)
                } else {
                    data.count -= 1
                    notifyItemChanged(holder.bindingAdapterPosition)
                }
            }
        }

        holder.cartItemBinding.deleteProduct.setOnClickListener {
            fun removeProduct() {
                products.remove(data)
                notifyItemRemoved(holder.bindingAdapterPosition)
            }
            removeProduct(data.id, ::removeProduct)
        }

        holder.cartItemBinding.increaseCount.setOnClickListener {
            increaseProduct(data.id) {
                data.count++
                notifyItemChanged(holder.bindingAdapterPosition)
            }
        }
    }

    fun getList(): List<BasketUiModel> {
        return products
    }

    fun updateList(list: List<BasketUiModel>) {
        products.clear()
        products.addAll(list)
        notifyDataSetChanged()
    }


}