package com.khanish.shopease.ui.auth.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.khanish.shopease.R
import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.FragmentRegisterBinding
import com.khanish.shopease.model.AuthResultModel
import com.khanish.shopease.utils.EditTextHelper
import com.khanish.shopease.utils.Helper
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment :
    BaseFragment<FragmentRegisterBinding>(
        FragmentRegisterBinding::inflate
    ) {
    private lateinit var editTextHelper: EditTextHelper
    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeData()
        editTextHelper = EditTextHelper(
            requireContext(),
            listOf(binding.inputFullName, binding.inputEmail, binding.inputPassword),
            binding.buttonRegister
        )

        binding.tvNavigateToLoginFragment.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
        binding.buttonRegister.setOnClickListener {
            registerAccount()
        }


    }

    private fun registerAccount() {
        val fullName = binding.inputFullName.text.toString().trim()
        val email = binding.inputEmail.text.toString().trim()
        val password = binding.inputPassword.text.toString().trim()

        viewModel.signUp(email, password)

    }

    private fun observeData() {
        viewModel.authModel.value = null
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
                it?.let {
                    if (it.isSuccess) {
                        findNavController().navigate(
                            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(
                                it.message
                            )
                        )

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
}