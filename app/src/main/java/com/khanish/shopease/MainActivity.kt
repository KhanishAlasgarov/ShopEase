package com.khanish.shopease

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationBarView
import com.khanish.shopease.databinding.ActivityMainBinding
import com.khanish.shopease.utils.gone
import com.khanish.shopease.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()




        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNav()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemWindowInsets = insets.systemWindowInsets
            view.setPadding(
                view.paddingLeft,
                systemWindowInsets.top,
                view.paddingRight,
                systemWindowInsets.bottom
            )
            insets
        }

    }

    private fun setBottomNav() {
        val manager =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavView, manager.navController)



        manager.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment, R.id.splashFragment,
                R.id.onboardingFragment -> {
                    binding.bottomNavView.gone()
                    binding.bottomBorder.gone()
                }

                else -> {
                    binding.bottomNavView.visible()
                    binding.bottomBorder.visible()
                }
            }
        }
    }
}