package com.khanish.shopease.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.khanish.shopease.R
import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.FragmentDetailBinding
import com.khanish.shopease.model.Size
import com.khanish.shopease.utils.Helper
import com.khanish.shopease.utils.changeFavoriteIcon
import com.khanish.shopease.utils.gone
import com.khanish.shopease.utils.visible
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(
    FragmentDetailBinding::inflate
) {
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel by viewModels<DetailViewModel>()
    private var size: Size? = null
    private val sizeAdapter = SizeAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeData()
        val id = args.id

        sizeAdapter.onClick = {
            size = it
        }

        binding.rvSizes.adapter = sizeAdapter



        viewModel.getAllSize()
        viewModel.getProductById(id)
        setBackButton()
        setFavoriteButton()
        setAddToBasketButton(id)
    }

    private fun setFavoriteButton() {
        binding.addToFavorite.setOnClickListener {
            viewModel.addToFavorite()
        }
    }

    private fun setBackButton() {
        binding.backButton.setOnClickListener {
//            findNavController().popBackStack(R.id.mainFragment, false)
            findNavController().navigateUp()
        }
    }

    private fun setAddToBasketButton(id: Int) {

        binding.addToCart.setOnClickListener {
            if (size == null) {
                FancyToast.makeText(
                    requireContext(),
                    "Please select size",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.WARNING,
                    false
                ).show()
                return@setOnClickListener
            }

            viewModel.addToBasket(id, size!!)
            FancyToast.makeText(
                requireContext(),
                "Product added to basket",
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                false
            ).show()

        }
    }


    private fun observeData() {
        val dialog = Helper.setSignUpDialog(layoutInflater, requireContext())
        with(viewModel) {
            product.observe(viewLifecycleOwner) {
                binding.product = it
                setProductViewPager(it!!.picUrls)
            }
            favoriteValue.observe(viewLifecycleOwner) {
                binding.favoriteIcon.changeFavoriteIcon(it)
            }



            sizes.observe(viewLifecycleOwner) {
                sizeAdapter.updateList(it)
            }

            loading.observe(viewLifecycleOwner) {
                if (it) {
                    dialog.show()
                    binding.mainSide.gone()
                } else {
                    dialog.dismiss()
                    binding.mainSide.visible()
                }
            }
        }
    }

    private fun setProductViewPager(picUrls: List<String>) {
        val adapter = ProductImageAdapter()
        adapter.updateList(picUrls)
        binding.viewPager2.adapter = adapter
        binding.wormDotsIndicator.attachTo(binding.viewPager2)
    }


}