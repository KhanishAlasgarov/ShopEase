package com.khanish.shopease.ui.main

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.marginRight
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.parser.ColorParser
import com.khanish.shopease.R
import com.khanish.shopease.databinding.CategoryItemBinding
import com.khanish.shopease.model.Category

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val categories = arrayListOf<Category>()
    lateinit var selectCategory: (categoryId: Int) -> Unit

    private var selectedPosition: Int = 0

    inner class ViewHolder(val categoryItemBinding: CategoryItemBinding) :
        RecyclerView.ViewHolder(categoryItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = categories[position]
        holder.categoryItemBinding.category = data

        holder.categoryItemBinding.categoryCard.setOnClickListener {
            if (selectedPosition != holder.bindingAdapterPosition) {
                val previousSelectedPosition = selectedPosition
                selectedPosition = holder.bindingAdapterPosition

                selectCategory(data.id)

                notifyItemChanged(selectedPosition)
                notifyItemChanged(previousSelectedPosition)
            }
        }

        if (selectedPosition == position) {
            holder.categoryItemBinding.apply {
                categoryCard.setCardBackgroundColor(
                    Color.parseColor("#1A1A1A")
                )
                categoryName.setTextColor(Color.parseColor("#FFFFFF"))
            }
        } else {
            holder.categoryItemBinding.apply {
                categoryCard.setCardBackgroundColor(
                    Color.parseColor("#E6E6E6")
                )
                categoryName.setTextColor(Color.parseColor("#1A1A1A"))
            }
        }

    }

    fun updateList(list: List<Category>) {
        categories.clear()
        categories.addAll(list)
        notifyDataSetChanged()
    }
}