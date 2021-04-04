package com.example.tryonlysports

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tryonlysports.location.ForegroundLocationService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

//import com.google.firebase.FirebaseApp

//private const val TAG = "MainActivity"
//private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

class MainActivity : AppCompatActivity() {
//    private var foregroundLocationServiceBound = false
//
//    // Provides location updates for while-in-use feature.
//    private var foregroundLocationService: ForegroundLocationService? = null
//
//    // Listens for location broadcasts from ForegroundOnlyLocationService.
//    private lateinit var foregroundBroadcastReceiver: ForegroundBroadcastReceiver

    lateinit var currentLocation: Location

    val db = FirebaseFirestore.getInstance()

    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var userId: String
    lateinit var emailId: String

//    private val foregroundOnlyServiceConnection = object : ServiceConnection {
//
//        override fun onServiceConnected(name: ComponentName, service: IBinder) {
//            val binder = service as ForegroundLocationService.LocalBinder
//            Log.i(TAG,"Connect to service.")
//            foregroundLocationService = binder.service
//            foregroundLocationServiceBound = true
//        }
//
//        override fun onServiceDisconnected(name: ComponentName) {
//            foregroundLocationService = null
//            foregroundLocationServiceBound = false
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = intent.getStringExtra("user_id")!!
        emailId = intent.getStringExtra("email_id")!!
        currentLocation = intent.getParcelableExtra<Location>("currentLocation")!!


//        btn_logout.setOnClickListener {
//            //Logout from app.
//            FirebaseAuth.getInstance().signOut()
//            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
//            finish()
//        }

//        val serviceIntent = Intent(this, ForegroundLocationService::class.java)
//        bindService(serviceIntent, foregroundOnlyServiceConnection, BIND_AUTO_CREATE)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//        foregroundBroadcastReceiver = ForegroundBroadcastReceiver()
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.personalInfoFragment, R.id.navigation_facility, R.id.navigation_sports, R.id.navigation_weather, R.id.activityListFragment))
        NavigationUI.setupActionBarWithNavController(this, navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

//    public fun subscribeToService() {
//        Log.i(TAG, "Asking for permission")
//        if (foregroundPermissionApproved()) {
//            foregroundLocationService?.subscribeToLocationUpdates()
//                ?: Log.d(TAG, "Service Not Bound")
//        } else {
//            requestForegroundPermissions()
//        }
//    }

//    override fun onStart() {
//        super.onStart()
//        Log.d(TAG, "onStartCalled")
////        FirebaseApp.initializeApp(this)
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//                foregroundBroadcastReceiver,
//                IntentFilter(
//                        ForegroundLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
//        )
//
//    }
//
//    override fun onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(
//                foregroundBroadcastReceiver
//        )
//        super.onPause()
//    }
//
//    override fun onStop() {
//        if (foregroundLocationServiceBound) {
//            unbindService(foregroundOnlyServiceConnection)
//            foregroundLocationServiceBound = false
//        }
//        super.onStop()
//    }
//
//
//    private fun requestForegroundPermissions() {
//        Log.d(TAG, "RequestForegroundPermission")
//        val provideRationale = foregroundPermissionApproved()
//
//        // If the user denied a previous request, but didn't check "Don't ask again", provide
//        // additional rationale.
//        if (provideRationale) {
//            Snackbar.make(
//                    findViewById(R.id.home_view),
//                    R.string.permission_rationale,
//                    Snackbar.LENGTH_LONG
//            )
//                    .setAction(R.string.ok) {
//                        // Request permission
//                        ActivityCompat.requestPermissions(
//                                this@MainActivity,
//                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
//                        )
//                    }
//                    .show()
//        } else {
//            Log.d(TAG, "Request foreground only permission")
//            ActivityCompat.requestPermissions(
//                    this@MainActivity,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
//            )
//        }
//    }
//
//    // TODO: Step 1.0, Review Permissions: Method checks if permissions approved.
//    private fun foregroundPermissionApproved(): Boolean {
//        Log.i(TAG, "CheckForegroundPermission")
//        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//    }
//
//    override fun onRequestPermissionsResult(
//            requestCode: Int,
//            permissions: Array<String>,
//            grantResults: IntArray
//    ) {
//        Log.d(TAG, "onRequestPermissionResult")
//
//        when (requestCode) {
//            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
//                grantResults.isEmpty() ->
//                    // If user interaction was interrupted, the permission request
//                    // is cancelled and you receive empty arrays.
//                    Log.d(TAG, "User interaction was cancelled.")
//                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
//                    // Permission was granted.
//                    foregroundLocationService?.subscribeToLocationUpdates()
//                else -> {
//                    // Permission denied.
//
//                    Snackbar.make(
//                            findViewById(R.id.home_view),
//                            R.string.permission_denied_explanation,
//                            Snackbar.LENGTH_LONG
//                    )
//                            .setAction(R.string.settings) {
//                                // Build intent that displays the App settings screen.
//                                val intent = Intent()
//                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                                val uri = Uri.fromParts(
//                                        "package",
//                                        BuildConfig.APPLICATION_ID,
//                                        null
//                                )
//                                intent.data = uri
//                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                                startActivity(intent)
//                            }
//                            .show()
//                }
//            }
//        }
//    }
//
//    /**
//     * Receiver for location broadcasts from [ForegroundOnlyLocationService].
//     */
//    private inner class ForegroundBroadcastReceiver : BroadcastReceiver() {
//
//        override fun onReceive(context: Context, intent: Intent) {
//            val location = intent.getParcelableExtra<Location>(
//                    ForegroundLocationService.EXTRA_LOCATION
//            )
//
//            if (location != null) {
//                Log.i(TAG, "Location obtained.")
//                currentLocation = location
//                foregroundLocationService?.unsubscribeToLocationUpdates()
//            }
//        }
//    }
//

}