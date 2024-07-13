package com.khanish.shopease.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter


@BindingAdapter("load_url")
fun loadUrl(imageView: ImageView, url: String) {
    imageView.loadImage(url)
}