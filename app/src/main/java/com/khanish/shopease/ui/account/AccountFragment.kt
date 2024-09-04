package com.khanish.shopease.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.khanish.shopease.R
import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.FragmentAccountBinding
import com.khanish.shopease.utils.Helper.Companion.setLogoutDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>(
    FragmentAccountBinding::inflate
) {
    val viewModel by viewModels<AccountViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setBackButton()
        setLogoutButton()


    }

    private fun setLogoutButton() {
        val dialog = setLogoutDialog(layoutInflater, requireContext()) {
            viewModel.logout()
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToRegisterFragment())

        }
        binding.buttonLogout.setOnClickListener {
            dialog.show()
        }

        binding.myDetailSide.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToMyDetailFragment())
        }

        binding.myOrdersSide.setOnClickListener {
            findNavController().navigate(
                AccountFragmentDirections
                    .actionAccountFragmentToMyOrderFragment()
            )
        }
    }


    private fun setBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}