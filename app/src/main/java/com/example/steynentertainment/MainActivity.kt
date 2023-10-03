package com.example.steynentertainment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.steynentertainment.databinding.ActivityMainBinding
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

        // Attach OnDestinationChangedListener to handle limited access
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (isLimitedAccess) {
                when (destination.id) {
                    R.id.navigation_members -> {
                        Toast.makeText(this, "Access to Members is limited.", Toast.LENGTH_SHORT).show()
                        navController.navigate(R.id.navigation_home)
                    }
                    R.id.navigation_profile -> {
                        Toast.makeText(this, "Access to Profile is limited.", Toast.LENGTH_SHORT).show()
                        navController.navigate(R.id.navigation_home)
                    }
                    else -> {
                        // Do nothing for other destinations
                    }
                }
            }
        }
    }
}
