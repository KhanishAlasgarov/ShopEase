package com.khanish.shopease.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.text.HtmlCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.khanish.shopease.R
import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.FragmentSearchBinding
import com.khanish.shopease.ui.main.MainViewModel
import com.khanish.shopease.ui.main.ProductAdapter
import com.khanish.shopease.utils.decorations.BottomSpacingItemDecoration
import com.khanish.shopease.utils.decorations.GridSpacingItemDecoration
import com.khanish.shopease.utils.gone
import com.khanish.shopease.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(
    FragmentSearchBinding::inflate
) {
    private val viewModel by viewModels<SearchViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()
    private val adapter = SearchItemAdapter()
    private val productAdapter = ProductAdapter()
    private val recentItemAdapter = RecentItemAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.fetchProducts()
        viewModel.getAllRecentItems()

        binding.rvSearchItems.adapter = adapter
        binding.rvProducts.adapter = productAdapter
        binding.rvRecentSearchItems.adapter = recentItemAdapter

        binding.rvRecentSearchItems.addItemDecoration(BottomSpacingItemDecoration(17))

        adapter.navigateDetail = {
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToDetailFragment(
                    it
                )
            )
        }

        productAdapter.navigateToDetail = {
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToDetailFragment(
                    it
                )
            )
        }

        productAdapter.addToFavorite = { product, callback ->
            mainViewModel.addProductToDb(product, callback)
        }


        recentItemAdapter.searchByRecentItem = {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
            binding.searchInput.clearFocus()
            binding.searchInput.setText(it)
            searchAction(it)

        }


        recentItemAdapter.deleteRecentItem = { id, callback ->
            viewModel.removeSearchItem(id, callback)
        }

        observeData()
        setClearAllTextView()
        setSearchInput()
        setBackButton()

    }

    override fun onResume() {
        super.onResume()
        resetInput()
    }

    private fun resetInput() {
        Log.e("Giydi", binding.searchInput.text.toString())
        binding.searchInput.text?.clear()
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
        binding.searchInput.clearFocus()

    }

    private fun observeData() {
        with(viewModel) {
            recentProducts.observe(viewLifecycleOwner) {
                val data = it.sortedByDescending { searchItem -> searchItem.id }
                Log.e("Recent Item", data.toString())
                recentItemAdapter.updateList(data)
            }

        }
    }

    private fun setBackButton() {
        with(binding) {
            backButton.setOnClickListener {
                findNavController().navigateUp()
            }

        }
    }

    private fun searchAction(value: String) {
        val products = viewModel.products.value
        val list = products!!.filter {
            it.name.lowercase().trim().contains(value)
        }

        if (list.isNotEmpty()) {
            with(binding) {
                rvProducts.visible()
                rvSearchItems.gone()
                recentSearchPlace.gone()
                notFoundPlace.gone()
            }
        } else {
            with(binding) {
                rvProducts.gone()
                rvSearchItems.gone()
                recentSearchPlace.gone()
                notFoundPlace.visible()
            }
        }

        productAdapter.updateList(list)
    }

    private fun setSearchInput() {

        binding.searchInput.setOnEditorActionListener { v, actionId, event ->

            val value = binding.searchInput.text.toString().trim()
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                v.clearFocus()

                searchAction(value)
                viewModel.setSearchItem(value)



                true
            } else
                false

        }

        binding.searchInput.addTextChangedListener { text ->
            val value = binding.searchInput.text.toString().trim().lowercase()
            val products = viewModel.products.value
            if (!value.isNullOrEmpty()) {
                val list = products!!.filter {
                    it.name.lowercase().trim().contains(value)
                }

                list?.let {
                    if (!it.isNullOrEmpty()) {
                        with(binding) {
                            rvProducts.gone()
                            rvSearchItems.visible()
                            recentSearchPlace.gone()
                            notFoundPlace.gone()
                        }
                    } else {
                        with(binding) {
                            rvProducts.gone()
                            rvSearchItems.gone()
                            recentSearchPlace.gone()
                            notFoundPlace.gone()
                        }
                    }

                    adapter.updateList(list)
                }

            } else {
                with(binding) {
                    rvProducts.gone()
                    rvSearchItems.gone()
                    notFoundPlace.gone()
                    recentSearchPlace.visible()
                }
            }


        }
    }

    private fun setClearAllTextView() {
        binding.tvClearAll.text = HtmlCompat.fromHtml(
            getString(R.string.clear_all),
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )

        binding.tvClearAll.setOnClickListener {
            viewModel.clearAllRecentItem()
        }
    }
}