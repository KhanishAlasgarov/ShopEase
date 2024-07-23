package com.khanish.shopease.model

import com.google.android.material.bottomsheet.BottomSheetDialog

data class FilterModel(
    val sortType: SortType,
    val minValue: Int,
    val maxValue: Int,
    val dialog: BottomSheetDialog
)