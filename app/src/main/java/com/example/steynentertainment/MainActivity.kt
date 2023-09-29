package com.example.steynentertainment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.steynentertainment.databinding.ActivityMainBinding
import com.example.steynentertainment.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isLimitedAccess: Boolean = false // Declare it here to make it accessible throughout the class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isLimitedAccess = intent.getBooleanExtra("LIMITED_ACCESS", false)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_events, R.id.navigation_members, R.id.navigation_aboutUs, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Handle item selection
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    true // Navigate to Home Fragment
                }
                R.id.navigation_members -> {
                    if (isLimitedAccess) {
                        Toast.makeText(this, "Access to Members is limited.", Toast.LENGTH_SHORT).show()
                        false // Cancel the navigation
                    } else {
                        true // Navigate to Members Fragment
                    }
                }
                R.id.navigation_profile -> {
                    if (isLimitedAccess) {
                        Toast.makeText(this, "Access to Profile is limited.", Toast.LENGTH_SHORT).show()
                        false // Cancel the navigation
                    } else {
                        //Toast.makeText(this, "Access to Profile is limited.", Toast.LENGTH_SHORT).show()

                        true // Navigate to Profile Fragment
                        //Toast.makeText(this, "Access to Profile is limited.", Toast.LENGTH_SHORT).show()

                    }
                }
                else -> {
                    true // This would mean that other items should not be restricted
                }
            }
        }
    }
}
