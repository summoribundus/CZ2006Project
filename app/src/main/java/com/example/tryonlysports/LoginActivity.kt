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
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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

/**
 * The tag of current login page
 */
private const val TAG = "LoginActivity"

/**
 * Enable the foreground permission for map service
 */
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

/**
 * This is the Activity for login the users.
 *
 * @author LI Yibai
 */
class LoginActivity : AppCompatActivity() {
    /**
     * This is the binding of the activity_login layout
     */
    private lateinit var binding: ActivityLoginBinding

    /**
     * This is the foreground location service bound.
     */
    private var foregroundLocationServiceBound = false

    /**
     * This is to provide location updates for while-in-use feature.
     */
    private var foregroundLocationService: ForegroundLocationService? = null

    /**
     * This is to listen for location broadcasts from ForegroundOnlyLocationService.
     */
    private lateinit var foregroundBroadcastReceiver: ForegroundBroadcastReceiver

    /**
     * This is the email that the user inputs.
     */
    private lateinit var email: String

    private val foregroundOnlyServiceConnection = object : ServiceConnection {
        /**
         * This function binds the service for location.
         *
         * @param name
         * @param service
         */
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundLocationService.LocalBinder
            Log.i(TAG,"Connect to service.")
            foregroundLocationService = binder.service
            foregroundLocationServiceBound = true
        }

        /**
         * This function stop the binding of the service of location.
         *
         * @param name
         */
        override fun onServiceDisconnected(name: ComponentName) {
            foregroundLocationService = null
            foregroundLocationServiceBound = false
        }
    }

    /**
     * This is the function of setting up layouts, buttons, and text input fields
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of MainActivity.
     * @return a View to display on the Login page.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foregroundBroadcastReceiver = ForegroundBroadcastReceiver()

//        binding.etLoginEmail.onFocusChangeListener = View.OnFocusChangeListener{ p0, p1->
//            if (!p1) hideSoftkeyboard(binding.etLoginEmail)
//        }
//
//        binding.etLoginPassword.onFocusChangeListener = View.OnFocusChangeListener{p0, p1 ->
//            if (!p1) hideSoftkeyboard(binding.etLoginPassword)
//        }



        binding.tvReg.setOnClickListener {

            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))

        }

        binding.btnLogin.setOnClickListener {
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
                                    subscribeToService()
                                    Toast.makeText(
                                            this@LoginActivity,
                                            "You were logged in successfully.",
                                            Toast.LENGTH_SHORT
                                    ).show()
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

    fun hideSoftkeyboard(editText: EditText) {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(editText.windowToken, 0)
        }
    }

    /**
     * This function asks the users permission for the location service.
     *
     */
    fun subscribeToService() {
        Log.i(TAG, "Asking for permission")
        if (foregroundPermissionApproved()) {
            foregroundLocationService?.subscribeToLocationUpdates()
                ?: Log.d(TAG, "Service Not Bound")
        } else {
            requestForegroundPermissions()
        }
    }

    /**
     * This function intent the service.
     *
     */
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStartCalled")
//        FirebaseApp.initializeApp(this)
        val serviceIntent = Intent(this, ForegroundLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, BIND_AUTO_CREATE)

    }

    /**
     * This function receive the foreground broadcast when resume the process.
     *
     */
    override fun onResume() {
        super.onResume()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            foregroundBroadcastReceiver,
            IntentFilter(
                ForegroundLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        )

    }

    /**
     * This function receive the foreground broadcast when pause the process.
     *
     */
    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
            foregroundBroadcastReceiver
        )
        super.onPause()
    }

    /**
     * This function unbinds the service when stop the process.
     *
     */
    override fun onStop() {
        if (foregroundLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundLocationServiceBound = false
        }
        super.onStop()
    }

    /**
     * This function request foreground permissions.
     *
     */
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

    /**
     * This function reviews if permissions are approved.
     *
     * @return the bool value of whether permits to the service.
     */
    private fun foregroundPermissionApproved(): Boolean {
        Log.i(TAG, "CheckForegroundPermission")
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    /**
     * This function requests the result of permissions
     *
     * @param requestCode is the permission code of the current permissions
     * @param permissions is the name of the current permissions
     * @param grantResults is an array of permission check result.
     */
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

        /**
         * This is the function that passes relevant information into the main activity
         *
         * @param context is an interface to global information about an application environment.
         * @param intent is an abstract description of an operation to be performed.
         */
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