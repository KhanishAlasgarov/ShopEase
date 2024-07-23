package com.khanish.shopease.ui.main

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khanish.shopease.databinding.CategoryItemBinding
import com.khanish.shopease.databinding.SortItemBinding
import com.khanish.shopease.model.Category
import com.khanish.shopease.model.SortModel

class SortAdapter : RecyclerView.Adapter<SortAdapter.ViewHolder>() {

    private val sortModels = arrayListOf<SortModel>()
    lateinit var selectedSortModel: (sortModel: SortModel) -> Unit

    private var selectedPosition: Int = 0

    inner class ViewHolder(val sortItemBinding: SortItemBinding) :
        RecyclerView.ViewHolder(sortItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SortItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sortModels.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = sortModels[position]
        holder.sortItemBinding.sortModel = data

        holder.sortItemBinding.categoryCard.setOnClickListener {
            if (selectedPosition != holder.adapterPosition) {
                val previousSelectedPosition = selectedPosition
                selectedPosition = holder.adapterPosition

                selectedSortModel(data)

                notifyItemChanged(selectedPosition)
                notifyItemChanged(previousSelectedPosition)
            }
        }

        if (selectedPosition == position) {
            holder.sortItemBinding.apply {
                categoryCard.setCardBackgroundColor(
                    Color.parseColor("#1A1A1A")
                )
                categoryName.setTextColor(Color.parseColor("#FFFFFF"))
            }
        } else {
            holder.sortItemBinding.apply {
                categoryCard.setCardBackgroundColor(
                    Color.parseColor("#E6E6E6")
                )
                categoryName.setTextColor(Color.parseColor("#1A1A1A"))
            }
        }

    }

    fun updateList(list: List<SortModel>) {
        sortModels.clear()
        sortModels.addAll(list)
        notifyDataSetChanged()
    }
}