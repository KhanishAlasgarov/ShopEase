package com.khanish.shopease.utils.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BottomSpacingItemDecoration(private val spaceWidth: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        if (position != parent.adapter?.itemCount?.minus(1)) {
            outRect.bottom = spaceWidth
        }
    }
}