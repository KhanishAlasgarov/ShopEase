package com.khanish.shopease.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.khanish.shopease.R


fun ImageView.loadImage(url: String) {
    Glide
        .with(this)
        .load(url)
        .into(this);
}

fun ImageView.changeFavoriteIcon(isFavorite: Boolean) {
    if (isFavorite) {
        this.setImageResource(R.drawable.heart_product_icon_red)
    } else {
        this.setImageResource(R.drawable.heart_product_icon)
    }
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}