package com.khanish.shopease.ui.detail


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khanish.shopease.databinding.SizeItemBinding
import com.khanish.shopease.databinding.SortItemBinding
import com.khanish.shopease.model.Size

class SizeAdapter : RecyclerView.Adapter<SizeAdapter.ViewHolder>() {

    private val sizes = arrayListOf<Size>()
    private var selectedPosition: Int = -1
    lateinit var onClick: (size: Size) -> Unit

    inner class ViewHolder(val sizeItemBinding: SizeItemBinding) :
        RecyclerView.ViewHolder(sizeItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SizeItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sizes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = sizes[position]
        holder.sizeItemBinding.textView27.text = data.size


        holder.sizeItemBinding.root.setOnClickListener {
            onClick(data)
            if (selectedPosition != holder.adapterPosition) {
                val previousSelectedPosition = selectedPosition
                selectedPosition = holder.adapterPosition

                notifyItemChanged(selectedPosition)
                notifyItemChanged(previousSelectedPosition)
            }
        }

        if (selectedPosition == position) {
            holder.sizeItemBinding.apply {
                root.setCardBackgroundColor(
                    Color.parseColor("#1A1A1A")
                )
                textView27.setTextColor(Color.parseColor("#FFFFFF"))
            }
        } else {
            holder.sizeItemBinding.apply {
                root.setCardBackgroundColor(
                    Color.parseColor("#00FFFFFF")
                )
                textView27.setTextColor(Color.parseColor("#1A1A1A"))
            }
        }


    }

    fun updateList(list: List<Size>) {
        sizes.clear()
        sizes.addAll(list)
        notifyDataSetChanged()
    }
}