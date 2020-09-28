package dev.similin.cloudchat.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.similin.cloudchat.R
import dev.similin.cloudchat.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigationItems()
    }

    private fun setupNavigationItems() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setSupportActionBar(binding.mainToolbar)
        appBarConfiguration =
            AppBarConfiguration.Builder(R.id.splashFragment, R.id.loginFragment, R.id.homeFragment)
                .build()
        binding.mainToolbar.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> setViewsGone()
                R.id.loginFragment -> setViewsGone()
                R.id.homeFragment -> setViewsVisible()
                R.id.settingsFragment -> setViewsVisible()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    private fun setViewsGone() {
        binding.mainToolbar.visibility = View.GONE
    }

    private fun setViewsVisible() {
        binding.mainToolbar.visibility = View.VISIBLE
    }

    fun setToolbarTitle(title: String) {
        binding.mainToolbar.title = title
    }
}