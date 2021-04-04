package com.example.tryonlysports

import android.Manifest
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.IBinder
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.tryonlysports.databinding.ActivityLoginBinding
import com.example.tryonlysports.location.ForegroundLocationService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

private const val TAG = "LoginActivity"
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var foregroundLocationServiceBound = false

    // Provides location updates for while-in-use feature.
    private var foregroundLocationService: ForegroundLocationService? = null

    // Listens for location broadcasts from ForegroundOnlyLocationService.
    private lateinit var foregroundBroadcastReceiver: ForegroundBroadcastReceiver

    private lateinit var email: String

    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundLocationService.LocalBinder
            Log.i(TAG,"Connect to service.")
            foregroundLocationService = binder.service
            foregroundLocationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundLocationService = null
            foregroundLocationServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foregroundBroadcastReceiver = ForegroundBroadcastReceiver()

        binding.tvReg.setOnClickListener {

            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))

        }

        binding.btnLogin.setOnClickListener {
            subscribeToService()
            when {
                TextUtils.isEmpty(binding.etLoginEmail.text.toString().trim { it <= ' ' }) -> {
                    val text = "Please enter email."
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(this@LoginActivity, text, duration)
                    toast.show()
                }

                TextUtils.isEmpty(binding.etLoginPassword.text.toString().trim { it <= ' ' }) -> {
                    val text = "Please enter password."
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(this@LoginActivity, text, duration)
                    toast.show()
                }
                else -> {

                    email = binding.etLoginEmail.text.toString().trim { it <= ' ' }
                    val password: String = binding.etLoginPassword.text.toString().trim { it <= ' ' }

                    //Log in using FirebaseAuth
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->

                                if (task.isSuccessful) {
                                    Toast.makeText(
                                            this@LoginActivity,
                                            "You were logged in successfully.",
                                            Toast.LENGTH_SHORT
                                    ).show()

//                                    val intent =
//                                            Intent(this@LoginActivity, MainActivity::class.java)
//                                    intent.flags =
//                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                    intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
//                                    intent.putExtra("email_id", email)
//                                    startActivity(intent)
//                                    finish()
                                } else {
                                    //If the login is not successful
                                    //The application shall display an error message
                                    Toast.makeText(
                                            this@LoginActivity,
                                            task.exception!!.message.toString(),
                                            Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                }
            }
        }
    }

    public fun subscribeToService() {
        Log.i(TAG, "Asking for permission")
        if (foregroundPermissionApproved()) {
            foregroundLocationService?.subscribeToLocationUpdates()
                ?: Log.d(TAG, "Service Not Bound")
        } else {
            requestForegroundPermissions()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStartCalled")
//        FirebaseApp.initializeApp(this)
        val serviceIntent = Intent(this, ForegroundLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, BIND_AUTO_CREATE)

    }

    override fun onResume() {
        super.onResume()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            foregroundBroadcastReceiver,
            IntentFilter(
                ForegroundLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        )

    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
            foregroundBroadcastReceiver
        )
        super.onPause()
    }

    override fun onStop() {
        if (foregroundLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundLocationServiceBound = false
        }
        super.onStop()
    }


    private fun requestForegroundPermissions() {
        Log.d(TAG, "RequestForegroundPermission")
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(
                findViewById(R.id.home_view),
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(this@LoginActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            ActivityCompat.requestPermissions(
                this@LoginActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    // TODO: Step 1.0, Review Permissions: Method checks if permissions approved.
    private fun foregroundPermissionApproved(): Boolean {
        Log.i(TAG, "CheckForegroundPermission")
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.d(TAG, "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    foregroundLocationService?.subscribeToLocationUpdates()
                else -> {
                    // Permission denied.

                    Snackbar.make(
                        findViewById(R.id.home_view),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                }
            }
        }
    }

    /**
     * Receiver for location broadcasts from [ForegroundOnlyLocationService].
     */
    private inner class ForegroundBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(
                ForegroundLocationService.EXTRA_LOCATION
            )

            if (location != null) {
                Log.i(TAG, "Location obtained.")
                foregroundLocationService?.unsubscribeToLocationUpdates()
                val intent =
                    Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                intent.putExtra("email_id", email)
                intent.putExtra("currentLocation", location)
                startActivity(intent)
                finish()
            }
        }
    }


}