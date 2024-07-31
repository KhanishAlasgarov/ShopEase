package com.khanish.shopease.ui.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.khanish.shopease.R
import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.FragmentSavedBinding
import com.khanish.shopease.utils.Helper
import com.khanish.shopease.utils.gone
import com.khanish.shopease.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : BaseFragment<FragmentSavedBinding>(
    FragmentSavedBinding::inflate
) {

    private val adapter = FavoriteProductAdapter()
    private val viewModel by viewModels<SavedViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeData()
        viewModel.fetchProducts()
        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.itemAnimator = DefaultItemAnimator()
        adapter.addToFavorite = { product ->
            viewModel.addProductToDb(product)
        }

        adapter.navigateToDetail = {
            findNavController().navigate(
                SavedFragmentDirections.actionSavedFragmentToDetailFragment(
                    it
                )
            )
        }

        adapter.checkCount = { products ->
            if (products.isEmpty()) {
                binding.noSavedItems.visible()
                binding.rvFavorite.gone()
            }

        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeData() {
        val dialog = Helper.setSignUpDialog(layoutInflater, requireContext())
        with(viewModel) {
            products.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    adapter.updateList(it)
                    binding.noSavedItems.gone()
                    binding.rvFavorite.visible()
                } else {
                    binding.noSavedItems.visible()
                    binding.rvFavorite.gone()
                }
            }

            loading.observe(viewLifecycleOwner) {
                if (it) {
                    dialog.show()
                } else {
                    dialog.dismiss()
                }
            }
        }
    }
}