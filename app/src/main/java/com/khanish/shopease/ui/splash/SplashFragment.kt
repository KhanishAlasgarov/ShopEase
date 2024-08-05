package com.khanish.shopease.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.khanish.shopease.R
import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : BaseFragment<FragmentSplashBinding>(
    FragmentSplashBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        lifecycleScope.launch {
            delay(3000)
            findNavController().navigate(
                SplashFragmentDirections
                    .actionSplashFragmentToOnboardingFragment()
            )

        }
    }

}