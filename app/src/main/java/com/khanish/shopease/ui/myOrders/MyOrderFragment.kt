package com.khanish.shopease.ui.myOrders

import android.os.Bundle

import android.view.View
import androidx.fragment.app.viewModels
import com.google.firestore.v1.StructuredQuery.Order

import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.FragmentMyOrderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOrderFragment : BaseFragment<FragmentMyOrderBinding>(
    FragmentMyOrderBinding::inflate
) {
    val viewModel by viewModels<MyOrderViewModel>()
    val adapter = OrderAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeData()
        viewModel.getOrders()
        binding.rvOrders.adapter = adapter
    }

    private fun observeData() {
        with(viewModel) {
            products.observe(viewLifecycleOwner) {
                adapter.updateList(it)
            }
        }
    }


}