package com.example.steynentertainment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.steynentertainment.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isLimitedAccess: Boolean = true
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Analytics
        firebaseAnalytics = Firebase.analytics

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isMember = intent.getBooleanExtra("IS_MEMBER", false)

        // Log the isMember value
        val bundle = Bundle()
        bundle.putBoolean("is_member", isMember)
        firebaseAnalytics.logEvent("is_member_check", bundle)

        if (intent.hasExtra("LIMITED_ACCESS")) {
            isLimitedAccess = intent.getBooleanExtra("LIMITED_ACCESS", true)
        }

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
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

            // Log limited access
            val limitedAccessBundle = Bundle()
            limitedAccessBundle.putBoolean("limited_access", true)
            firebaseAnalytics.logEvent("limited_access", limitedAccessBundle)
        }
    }
}
