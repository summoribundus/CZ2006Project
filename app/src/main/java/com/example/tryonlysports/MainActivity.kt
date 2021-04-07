package com.example.tryonlysports

import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Main activity
 *
 * This class is used for hosting main fuctionalities
 */
class MainActivity : AppCompatActivity() {

    /**
     * Current location
     */
    lateinit var currentLocation: Location

    /**
     * Database instance
     */
    val db = FirebaseFirestore.getInstance()

    /**
     * Button app bar configuration
     */
    private lateinit var appBarConfiguration: AppBarConfiguration

    /**
     * Current user id
     */
    lateinit var userId: String

    /**
     * Current user email
     */
    lateinit var emailId: String

    /**
     * Set up location, userid, email, and button navigation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = intent.getStringExtra("user_id")!!
        emailId = intent.getStringExtra("email_id")!!
        currentLocation = intent.getParcelableExtra<Location>("currentLocation")!!

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.personalInfoFragment, R.id.navigation_facility, R.id.navigation_sports, R.id.navigation_weather, R.id.activityListFragment))
        NavigationUI.setupActionBarWithNavController(this, navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    /**
     * Support top return button
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}