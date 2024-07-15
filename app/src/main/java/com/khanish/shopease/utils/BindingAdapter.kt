package com.khanish.shopease.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.khanish.shopease.R


@BindingAdapter("load_url")
fun loadUrl(imageView: ImageView, url: String) {
    imageView.loadImage(url)
}

@BindingAdapter("favoriteImage")
fun setFavoriteImage(view: ImageView, isFavorite: Boolean) {
    if (isFavorite) {
        view.setImageResource(R.drawable.heart_product_icon_red)
    } else {
        view.setImageResource(R.drawable.heart_product_icon)
    }
}