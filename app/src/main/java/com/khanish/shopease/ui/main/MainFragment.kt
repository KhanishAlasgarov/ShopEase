package com.khanish.shopease.ui.main

import android.os.Bundle
import android.util.Log

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.khanish.shopease.R
import com.khanish.shopease.base.BaseFragment
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

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(
    FragmentMainBinding::inflate
) {
    private val adapter = CategoryAdapter()
    private val productAdapter = ProductAdapter()
    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeData()

        adapter.selectCategory = {
            viewModel.fetchData(it)
        }
        viewModel.fetchData(null)

        binding.rvProducts.adapter = productAdapter
        binding.rvCategories.adapter = adapter
        binding.rvCategories.itemAnimator = null

        val spaceWidth = resources.getDimensionPixelSize(R.dimen.category_item_spacing)
        binding.rvCategories.addItemDecoration(
            CustomItemDecoration(
                spaceWidth
            )
        )
    }


    private fun observeData() {
        val dialog = Helper.setSignUpDialog(layoutInflater, requireContext())
        with(viewModel) {
            loading.observe(viewLifecycleOwner) {
                if (it) {
                    dialog.show()
                    binding.mainPlace.gone()
                } else {
                    dialog.dismiss()
                    binding.mainPlace.visible()
                }
            }

            categories.observe(viewLifecycleOwner) {
                adapter.updateList(it)
            }

            products.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()){
                    productAdapter.updateList(it)
                    binding.notFoundPlace.gone()
                    binding.rvProducts.visible()
                }else{
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
        }
    }


}


