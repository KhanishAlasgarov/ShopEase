package com.khanish.shopease.ui.onboarding

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
        }
    }
}