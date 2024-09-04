package com.khanish.shopease.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.khanish.shopease.R
import com.khanish.shopease.databinding.BottomSheetFilterDialogBinding
import com.khanish.shopease.databinding.CheckoutDialogBinding
import com.khanish.shopease.databinding.FragmentMainBinding
import com.khanish.shopease.databinding.LoadingBinding
import com.khanish.shopease.databinding.LogoutDialogBinding
import com.khanish.shopease.model.Product
import com.khanish.shopease.model.SortModel
import com.khanish.shopease.model.SortType
import com.khanish.shopease.ui.main.MainViewModel
import com.khanish.shopease.ui.main.SortAdapter
import com.khanish.shopease.utils.decorations.CustomItemDecoration
import kotlin.math.ceil

class Helper {

    companion object {
        fun setSignUpDialog(
            layoutInflater: LayoutInflater,
            requireContext: Context
        ): AlertDialog {

            val dialogBinding = LoadingBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(requireContext).create()

            dialog.setView(dialogBinding.root)
            dialog.setCancelable(false)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return dialog

        }

        fun setLogoutDialog(
            layoutInflater: LayoutInflater,
            requireContext: Context,
            logoutProcess: () -> Unit
        ): AlertDialog {
            val dialogBinding = LogoutDialogBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(requireContext).create()

            dialog.setView(dialogBinding.root)
            dialog.setCancelable(false)

            dialogBinding.logout.setOnClickListener {
                logoutProcess()
                dialog.dismiss()
            }

            dialogBinding.cancel.setOnClickListener {
                dialog.dismiss()
            }

            return dialog

        }

        fun setCompleteDialog(
            layoutInflater: LayoutInflater,
            requireContext: Context,
            callback: () -> Unit
        ) {
            val dialogBinding = CheckoutDialogBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(requireContext).create()

            dialogBinding.button.setOnClickListener {
                callback()
                dialog.dismiss()
            }
            dialog.setView(dialogBinding.root)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        fun setUpSheetDialog(
            layoutInflater: LayoutInflater, context: Context,
            binding: FragmentMainBinding,
            viewModel: MainViewModel,
            viewLifecycleOwner: LifecycleOwner
        ): BottomSheetFilterDialogBinding {

            val dialog = BottomSheetDialog(context)
            val dialogSheetBinding = BottomSheetFilterDialogBinding.inflate(layoutInflater)

            setUpFilterButton(binding, dialog, dialogSheetBinding, viewModel)
            observeViewModel(dialogSheetBinding, viewModel, viewLifecycleOwner)

            dialogSheetBinding.rangeSlider.setCustomThumbDrawable(R.drawable.thumb_rangeslider)
            dialog.setCancelable(true)
            dialog.setContentView(dialogSheetBinding.root)

            return dialogSheetBinding
        }


        private fun setUpFilterButton(
            binding: FragmentMainBinding,
            dialog: BottomSheetDialog,
            dialogSheetBinding: BottomSheetFilterDialogBinding,
            viewModel: MainViewModel
        ) {

            var sortModel: SortModel? = null
            var minValue: Int? = null
            var maxValue: Int? = null


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

            adapter.selectedSortModel = { it ->
                sortModel = it
            }
            dialogSheetBinding.recyclerView.itemAnimator = null
            dialogSheetBinding.recyclerView.adapter = adapter
            dialogSheetBinding.recyclerView.addItemDecoration(
                CustomItemDecoration(
                    8
                )
            )

            binding.btnFilter.setOnClickListener {
                adapter.resetSelectedPosition()
                dialogSheetBinding.rangeSlider.values = viewModel.rangeSliderValues.value!!
                dialogSheetBinding.rangeSlider.addOnChangeListener { slider, _, _ ->

                    val values = slider.values
                    minValue = ceil(values[0]).toInt()
                    maxValue = ceil(values[1]).toInt()

                    viewModel.minValue.value = minValue
                    viewModel.maxValue.value = maxValue
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


            val products = if (viewModel.searchedProducts.value != null) {
                viewModel.searchedProducts.value
            } else {
                viewModel.products.value
            }

            products?.let { productsList ->
                var newProductList = emptyList<Product>()

                if (sortModel != null) {
                    when (sortModel.type) {
                        SortType.LowToHigh -> {
                            newProductList = productsList.sortedBy { i -> i.price }

                        }

                        SortType.HighToLow -> {
                            newProductList = productsList.sortedByDescending { i -> i.price }
                        }

                        SortType.Relevance -> {
                            newProductList = productsList.shuffled()
                        }

                        SortType.ProductName -> {
                            newProductList = productsList.sortedBy { i -> i.name }
                        }

                    }
                } else {
                    newProductList = productsList.shuffled()
                }

                minValue?.let { min ->
                    maxValue?.let { max ->
                        newProductList = newProductList.filter { filterValue ->
                            filterValue.price >= min && filterValue.price <= max
                        }
                    }
                }

                viewModel.filteredData.value = newProductList
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

            viewModel.rangeSliderValues.observe(viewLifecycleOwner) { values ->
                dialogSheetBinding.rangeSlider.valueFrom = values[0]
                dialogSheetBinding.rangeSlider.valueTo = values[1]
                dialogSheetBinding.rangeSlider.values = values

            }
        }
    }


}



