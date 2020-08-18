package dev.similin.cloudchat.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dev.similin.cloudchat.R
import dev.similin.cloudchat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigationItems()
    }

    private fun setupNavigationItems() {
        navController = findNavController(R.id.my_nav_host_fragment)
        setSupportActionBar(binding.mainToolbar)
        val appBarConfiguration =
            AppBarConfiguration.Builder(R.id.splashFragment, R.id.loginFragment).build()
        binding.mainToolbar.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> setViewsGone()
                R.id.loginFragment -> setViewsGone()
            }
        }
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