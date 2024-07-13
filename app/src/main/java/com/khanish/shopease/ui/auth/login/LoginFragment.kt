package com.khanish.shopease.ui.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.khanish.shopease.R
import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.FragmentLoginBinding
import com.khanish.shopease.repository.AuthRepository
import com.khanish.shopease.utils.EditTextHelper
import com.khanish.shopease.utils.Helper
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {

    private val args: LoginFragmentArgs by navArgs()
    private lateinit var editTextHelper: EditTextHelper
    private val viewModel: LoginViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val message = args.message
        observeData()
        editTextHelper = EditTextHelper(
            requireContext(),
            listOf(binding.inputEmail, binding.inputPassword),
            binding.buttonLogin
        )

        message?.let {
            FancyToast.makeText(
                requireContext(),
                it,
                FancyToast.LENGTH_LONG,
                FancyToast.SUCCESS,
                false
            ).show()
        }

        binding.buttonLogin.setOnClickListener {
            loginAccount()
        }
    }

    private fun loginAccount() {
        val email = binding.inputEmail.text.toString().trim()
        val password = binding.inputPassword.text.toString().trim()
        viewModel.signIn(email, password)
    }

    private fun observeData() {

        with(viewModel) {
            val dialog = Helper.setSignUpDialog(layoutInflater, requireContext())
            loading.observe(viewLifecycleOwner) {
                if (it) {
                    dialog.show()
                } else {
                    dialog.dismiss()
                }
            }
            authModel.observe(viewLifecycleOwner) {
                if (it.isSuccess) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                } else {
                    FancyToast.makeText(
                        requireContext(),
                        it.message,
                        FancyToast.LENGTH_LONG,
                        FancyToast.ERROR,
                        false
                    ).show()
                }

            }
        }
    }

}