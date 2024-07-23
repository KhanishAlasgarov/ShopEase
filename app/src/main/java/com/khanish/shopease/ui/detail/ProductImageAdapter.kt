package com.khanish.shopease.ui.detail

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.khanish.shopease.databinding.ProductDetailImageBinding
import com.khanish.shopease.utils.gone
import com.khanish.shopease.utils.visible

class ProductImageAdapter : RecyclerView.Adapter<ProductImageAdapter.ViewHolder>() {

    private val images = arrayListOf<String>()

    inner class ViewHolder(val productDetailImageBinding: ProductDetailImageBinding) :
        RecyclerView.ViewHolder(productDetailImageBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ProductDetailImageBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = images[position]

        holder.productDetailImageBinding.progressBar.visible()
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.productDetailImageBinding.progressBar.gone()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.productDetailImageBinding.progressBar.gone()
                    return false
                }

            })
            .into(holder.productDetailImageBinding.imageView)

    }

    fun updateList(list: List<String>) {
        images.clear()
        images.addAll(list)
        notifyDataSetChanged()
    }
}