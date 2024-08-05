package com.khanish.shopease.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.FragmentCartBinding
import com.khanish.shopease.model.BasketUiModel
import com.khanish.shopease.utils.Helper
import com.khanish.shopease.utils.decorations.BottomSpacingItemDecoration
import com.khanish.shopease.utils.gone
import com.khanish.shopease.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(
    FragmentCartBinding::inflate
) {
    private val viewModel: CartViewModel by viewModels()
    private val adapter: BasketProductAdapter = BasketProductAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getBasketItems()
        observeData()
        setAdapter()
        setBackButton()
    }

    private fun setBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setAdapter() {
        binding.rvBasketProducts.adapter = adapter
        binding.rvBasketProducts.addItemDecoration(BottomSpacingItemDecoration(14))

        adapter.removeProduct = { id, callback ->

            fun removeItem() {
                callback()
                updateBasketUi(adapter.getList())
            }
            viewModel.removeProduct(id, ::removeItem)

        }

        adapter.deacreseProduct = { id, callback ->
            viewModel.decreaseProduct(id, callback)
            updateBasketUi(adapter.getList())
        }
        adapter.increaseProduct = { id, callback ->
            viewModel.increaseProduct(id, callback)
            updateBasketUi(adapter.getList())

        }

    }

    private fun observeData() {
        val dialog = Helper.setSignUpDialog(layoutInflater, requireContext())
        with(viewModel) {
            products.observe(viewLifecycleOwner) {
                adapter.updateList(it)
                updateBasketUi(it)

            }
            loading.observe(viewLifecycleOwner) {

                if (it) {
                    dialog.show()
                    binding.myCartMainPlace.gone()
                } else {
                    dialog.dismiss()
                    binding.myCartMainPlace.visible()
                }
            }
        }
    }

    private fun updateBasketUi(list: List<BasketUiModel>) {
        if (list.isNotEmpty()) {
            binding.cartSide.visible()
            binding.emptyCart.gone()
            val sum = calculatePrice(list)
            val vat = (sum * 18 / 100)
            val shipping = (list.count() * 5).toDouble()
            val total = sum + vat + shipping

            binding.tvSubTotal.text = String.format("%.2f", sum)
            binding.tvVat.text = String.format("%.2f", vat)
            binding.tvShipping.text = String.format("%.2f", shipping)

            binding.tvTotal.text = String.format("%.2f", total)
        } else {
            binding.cartSide.gone()
            binding.emptyCart.visible()

        }

    }

    private fun calculatePrice(list: List<BasketUiModel>): Double {
        var total = 0.0

        for (product in list) {
            total += (product.price * product.count)
        }

        return total
    }
}