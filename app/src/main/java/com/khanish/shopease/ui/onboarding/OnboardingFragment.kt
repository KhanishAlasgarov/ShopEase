package com.khanish.shopease.ui.onboarding

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.khanish.shopease.R
import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.FragmentOnboardingBinding


class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>(
    FragmentOnboardingBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonGetStarted.setOnClickListener {
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToRegisterFragment())
            onBoardingFinished()
        }
    }


    private fun onBoardingFinished() {
        val sharedPreferences = requireActivity()
            .getSharedPreferences("MySharedPref", MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        editor.putBoolean("onBoarding", true)
        editor.apply()
    }
}