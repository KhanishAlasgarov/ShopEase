package com.khanish.shopease.ui.main

import android.os.Bundle
import android.util.Log

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.khanish.shopease.R
import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.BottomSheetFilterDialogBinding
import com.khanish.shopease.databinding.FragmentMainBinding
import com.khanish.shopease.model.Category

import com.khanish.shopease.utils.CustomItemDecoration
import com.khanish.shopease.utils.Helper
import com.khanish.shopease.utils.gone
import com.khanish.shopease.utils.visible
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.math.ceil

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(
    FragmentMainBinding::inflate
) {
    lateinit var adapter: CategoryAdapter

    private val viewModel by viewModels<MainViewModel>()
    private val productAdapter = ProductAdapter()

//    lateinit var sheetDialog: BottomSheetFilterDialogBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = CategoryAdapter()
//        sheetDialog = Helper.setUpSheetDialog(
//            layoutInflater,
//            requireContext(),
//            binding,
//            viewModel,
//            viewLifecycleOwner
//        )
        observeData()
        setUpRecyclerViews()
        fetchInitialData()

    }

    private fun fetchInitialData() {
        viewModel.fetchData(null)
    }

    private fun setUpRecyclerViews() {
        adapter.selectCategory = {
            viewModel.fetchData(it)
        }


        productAdapter.addToFavorite = { product, callback ->

            fun changeProperty(isFavorite: Boolean) {
                callback(isFavorite)
            }

            viewModel.addProductToDb(product, ::changeProperty)


        }

        productAdapter.navigateToDetail = { id ->
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailFragment(
                    id
                )
            )
        }

        val spaceWidth = resources.getDimensionPixelSize(R.dimen.category_item_spacing)

        binding.rvCategories.addItemDecoration(
            CustomItemDecoration(
                spaceWidth
            )
        )

        binding.rvProducts.adapter = productAdapter
        binding.rvCategories.adapter = adapter
        binding.rvCategories.itemAnimator = null

    }

    private fun observeData() {
        val dialog = Helper.setSignUpDialog(layoutInflater, requireContext())
        with(viewModel) {

            categories.observe(viewLifecycleOwner) {
                adapter.updateList(it)
            }

            products.observe(viewLifecycleOwner) {
                productAdapter.updateList(it)
                if (it.isNotEmpty()) {

                    val data = it.maxOf { it.price }
                    val minValue = 0.toFloat()
                    val maxValue = ceil(data).toInt().toFloat()

//                    sheetDialog.rangeSlider.valueFrom = minValue
//                    sheetDialog.rangeSlider.valueTo = maxValue
//                    sheetDialog.rangeSlider.values = listOf(minValue, maxValue)
//                    sheetDialog.minValue.text = minValue.toString()
//                    sheetDialog.maxValue.text = maxValue.toString()

                    productAdapter.updateList(it)
                    binding.notFoundPlace.gone()
                    binding.rvProducts.visible()
                } else {
                    binding.notFoundPlace.visible()
                    binding.rvProducts.gone()
                }
            }

            error.observe(viewLifecycleOwner) {
                FancyToast.makeText(
                    requireContext(),
                    it,
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false
                ).show()
            }

            productLoading.observe(viewLifecycleOwner){
                if (it){
                    binding.rvProductsContainer.gone()
                    binding.progressBar2.visible()
                }else{
                    binding.rvProductsContainer.visible()
                    binding.progressBar2.gone()
                }
            }
        }
    }


}


