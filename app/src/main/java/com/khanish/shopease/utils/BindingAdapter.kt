package com.khanish.shopease.utils

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.khanish.shopease.R


@BindingAdapter("load_url")
fun loadUrl(imageView: ImageView, url: String) {
    imageView.loadImage(url)
}

@BindingAdapter("favoriteImage")
fun setFavoriteImage(view: ImageView, isFavorite: Boolean) {
    view.changeFavoriteIcon(isFavorite)
}