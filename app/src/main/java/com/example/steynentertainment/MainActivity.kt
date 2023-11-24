package com.example.steynentertainment

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.steynentertainment.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isLimitedAccess: Boolean = true
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Analytics and Auth
        firebaseAnalytics = Firebase.analytics
        firebaseAuth = FirebaseAuth.getInstance()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check Firebase Auth for current user
        isLimitedAccess = firebaseAuth.currentUser == null

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.mobile_navigation).apply {
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

            val onBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!navController.navigateUp()) {
                        if (isEnabled) {
                            isEnabled = false
                            onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }
            }

            // Register the callback with the OnBackPressedDispatcher
            onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
