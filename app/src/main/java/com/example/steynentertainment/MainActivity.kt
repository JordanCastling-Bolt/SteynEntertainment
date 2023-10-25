package com.example.steynentertainment

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.steynentertainment.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.NavGraph

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isLimitedAccess: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isMember = intent.getBooleanExtra("IS_MEMBER", false)
        Toast.makeText(this, "isMember: $isMember", Toast.LENGTH_SHORT).show() // Debug line

        if (intent.hasExtra("LIMITED_ACCESS")) {
            isLimitedAccess = intent.getBooleanExtra("LIMITED_ACCESS", true)
        }

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.mobile_navigation).apply {
            // Always set the start destination to fragment_home
            setStartDestination(R.id.navigation_home)
        }

        navController.graph = graph // Set the modified NavGraph to the NavController

        val navView: BottomNavigationView = binding.navView
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_events,
                R.id.navigation_members,
                R.id.navigation_aboutUs,
                R.id.navigation_profile
            )
        )

        // Setup action bar and bottom navigation
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Conditionally disable or hide navigation items
        if (isLimitedAccess) {
            val menu = navView.menu
            menu.findItem(R.id.navigation_members)?.isVisible = false // hide Members tab
            menu.findItem(R.id.navigation_profile)?.isVisible = false // hide Profile tab

            //Make sure the sign-in button disappears when the user is signed in
            val siginbutton = findViewById<Button>(R.id.btnLogin)
            siginbutton.isVisible = true
        }
    }
}
