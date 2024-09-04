package com.khanish.shopease.ui.splash

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.khanish.shopease.R
import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(
    FragmentSplashBinding::inflate
) {
    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val user = viewModel.getUser()

        Log.e("user", user?.email.toString())

        lifecycleScope.launch {
            delay(3000)

            if (onBoardingFinished()) {
                if (user == null) {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToRegisterFragment())
                } else {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToMainFragment())
                }

            } else {
                findNavController().navigate(
                    SplashFragmentDirections
                        .actionSplashFragmentToOnboardingFragment()
                )
            }


        }
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPreferences = requireActivity()
            .getSharedPreferences("MySharedPref", MODE_PRIVATE)

        val isFinished = sharedPreferences.getBoolean("onBoarding", false)

        return isFinished
    }

}