package com.khanish.shopease.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.khanish.shopease.R
import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.BottomSheetFilterDialogBinding
import com.khanish.shopease.databinding.FragmentMainBinding
import com.khanish.shopease.model.Product
import com.khanish.shopease.utils.decorations.CustomItemDecoration
import com.khanish.shopease.utils.Helper
import com.khanish.shopease.utils.gone
import com.khanish.shopease.utils.visible
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(
    FragmentMainBinding::inflate
) {
    lateinit var adapter: CategoryAdapter

    private val viewModel by viewModels<MainViewModel>()
    private val productAdapter = ProductAdapter()
    private lateinit var bottomNavigationView: BottomNavigationView

    lateinit var sheetDialog: BottomSheetFilterDialogBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.rvProducts.itemAnimator = null
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavView)
        adapter = CategoryAdapter()
        sheetDialog = Helper.setUpSheetDialog(
            layoutInflater,
            requireContext(),
            binding,
            viewModel,
            viewLifecycleOwner
        )
        observeData()
        setUpRecyclerViews()
        fetchInitialData()

        binding.searchInput.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                v.clearFocus()
                true
            } else {
                false
            }
        }

        binding.searchInput.addTextChangedListener { text ->
            searchAction()
        }


    }

    private fun searchAction() {

        val list = viewModel.products.value
        val inputValue = binding.searchInput.text.toString().trim().lowercase()

        list?.let {
            if (!inputValue.isNullOrEmpty()) {
                val newList =
                    list.filter {
                        it.name.lowercase().trim().contains(inputValue)
                    }
                productAdapter.updateList(newList)
                if (newList.isNotEmpty()) {
                    binding.notFoundPlace.gone()
                    binding.rvProducts.visible()

                    setRangeSlider(newList)
                } else {
                    binding.notFoundPlace.visible()
                    binding.rvProducts.gone()
                }

                viewModel.searchedProducts.value = newList

                productAdapter.updateList(newList)
            } else {
                viewModel.searchedProducts.value = null
                productAdapter.updateList(list)
            }
        }


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

    private fun setRangeSlider(products: List<Product>) {
        val data = products.maxOf { it.price }
        val minValue = 0.toFloat()
        val maxValue = ceil(data).toInt().toFloat()

        viewModel.minValue.value = minValue.toInt()
        viewModel.maxValue.value = maxValue.toInt()
        viewModel.rangeSliderValues.value = arrayListOf(minValue, maxValue)
    }

    private fun observeData() {
        with(viewModel) {
            categories.observe(viewLifecycleOwner) {
                adapter.updateList(it)
            }

            products.observe(viewLifecycleOwner) {

                if (it.isNotEmpty()) {

                    setRangeSlider(it)
                    productAdapter.updateList(it)
                    binding.notFoundPlace.gone()
                    binding.rvProducts.visible()
                } else {
                    binding.notFoundPlace.visible()
                    binding.rvProducts.gone()
                }
            }
            filteredData.observe(viewLifecycleOwner) {

                productAdapter.updateList(it)
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

            productLoading.observe(viewLifecycleOwner) {
                if (it) {
                    binding.rvProductsContainer.gone()
                    binding.progressBar2.visible()
                } else {
                    binding.rvProductsContainer.visible()
                    binding.progressBar2.gone()
                }
            }
        }
    }


}


