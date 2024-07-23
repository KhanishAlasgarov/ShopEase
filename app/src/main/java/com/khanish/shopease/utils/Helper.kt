package com.khanish.shopease.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.khanish.shopease.R
import com.khanish.shopease.databinding.BottomSheetFilterDialogBinding
import com.khanish.shopease.databinding.FragmentMainBinding
import com.khanish.shopease.databinding.LoadingBinding
import com.khanish.shopease.model.SortModel
import com.khanish.shopease.model.SortType
import com.khanish.shopease.ui.main.CategoryAdapter
import com.khanish.shopease.ui.main.MainViewModel
import com.khanish.shopease.ui.main.SortAdapter
import java.util.Collections
import kotlin.math.ceil

class Helper {

    companion object {
        fun setSignUpDialog(
            layoutInflater: LayoutInflater,
            requireContext: Context
        ): AlertDialog {

            val dialogBinding = LoadingBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(requireContext).create();


            dialog.setView(dialogBinding.root)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return dialog

        }

        fun setUpSheetDialog(
            layoutInflater: LayoutInflater, context: Context,
            binding: FragmentMainBinding,
            viewModel: MainViewModel,
            viewLifecycleOwner: LifecycleOwner
        ): BottomSheetFilterDialogBinding {

            val dialog = BottomSheetDialog(context)
            val dialogSheetBinding = BottomSheetFilterDialogBinding.inflate(layoutInflater)

            setUpRecyclerView(dialogSheetBinding)
            setUpFilterButton(binding, dialog, dialogSheetBinding, viewModel)
            observeViewModel(dialogSheetBinding, viewModel, viewLifecycleOwner)

            dialogSheetBinding.rangeSlider.setCustomThumbDrawable(R.drawable.thumb_rangeslider)
            dialog.setCancelable(true)
            dialog.setContentView(dialogSheetBinding.root)

            return dialogSheetBinding
        }

        private fun setUpRecyclerView(dialogSheetBinding: BottomSheetFilterDialogBinding) {
            val adapter = SortAdapter().apply {
                updateList(
                    listOf(
                        SortModel("Relevance", SortType.Relevance),
                        SortModel("Product name", SortType.ProductName),
                        SortModel("Price: Low - High", SortType.LowToHigh),
                        SortModel("Price: High - Low", SortType.HighToLow)
                    )
                )
            }

            adapter.selectedSortModel = {
                Log.e("Salam", it.toString())
            }
            dialogSheetBinding.recyclerView.itemAnimator = null
            dialogSheetBinding.recyclerView.adapter = adapter
        }

        private fun setUpFilterButton(
            binding: FragmentMainBinding,
            dialog: BottomSheetDialog,
            dialogSheetBinding: BottomSheetFilterDialogBinding,
            viewModel: MainViewModel
        ) {
            val sortModel: SortModel? = null
            var minValue: Int? = null
            var maxValue: Int? = null

            binding.btnFilter.setOnClickListener {
                dialogSheetBinding.rangeSlider.addOnChangeListener { slider, _, _ ->
                    val values = slider.values
                    minValue = ceil(values[0]).toInt()
                    maxValue = ceil(values[1]).toInt()
                    viewModel.setTextRangeSliderValues(minValue!!, maxValue!!)
                }

                dialogSheetBinding.applyFilters.setOnClickListener {
                    applyFilters(viewModel, sortModel, minValue, maxValue)
                    dialog.dismiss()
                }

                dialogSheetBinding.btnCloseSheetDialog.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }
        }

        private fun applyFilters(
            viewModel: MainViewModel,
            sortModel: SortModel?,
            minValue: Int?,
            maxValue: Int?
        ) {
            if (minValue == null || maxValue == null) return

            viewModel.fetchData(null)
            var products = viewModel.products.value
            products?.let {
                sortModel?.let { sort ->
                    products = when (sort.type) {
                        SortType.LowToHigh -> it.sortedBy { it.price }
                        SortType.HighToLow -> it.sortedByDescending { it.price }
                        SortType.Relevance, null -> it.shuffled()
                        SortType.ProductName -> it.sortedBy { it.name }
                    }
                }
                products = it.filter {
                    it.price > minValue.toFloat() && it.price < maxValue.toFloat()
                }
                viewModel.setProducts(it)
            }
        }

        private fun observeViewModel(
            dialogSheetBinding: BottomSheetFilterDialogBinding,
            viewModel: MainViewModel,
            viewLifecycleOwner: LifecycleOwner
        ) {
            viewModel.minValue.observe(viewLifecycleOwner) {
                dialogSheetBinding.minValue.text = it.toString()
            }

            viewModel.maxValue.observe(viewLifecycleOwner) {
                dialogSheetBinding.maxValue.text = it.toString()
            }
        }
    }


}



