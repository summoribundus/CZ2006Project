package com.example.tryonlysports.location

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.tryonlysports.MainActivity
import com.google.android.gms.location.*
import com.example.tryonlysports.R

/**
 * location service
 *
 * This class is used for creating location service
 *
 * @author Li Rui
 */
class ForegroundLocationService: Service(){
    /**
     * whether configuration has changed
     */
    private var configurationChange = false

    /**
     * whether the service is running in foreground
     */
    private var serviceRunningInForeground = false

    /**
     * The local binder
     */
    private val localBinder = LocalBinder()

    /**
     * Manager for notification send out
     */
    private lateinit var notificationManager: NotificationManager

    /**
     * Main class for receiving location updates.
     */
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    /**
     * LocationRequest - Requirements for the location updates, i.e., how often you should receive updates, the priority, etc.
     */
    private lateinit var locationRequest: LocationRequest

    /**
     * Called when FusedLocationProviderClient has a new Location.
     */
    private lateinit var locationCallback: LocationCallback

    /**
     * Used for local storage of the last known location.
     */
    private var currentLocation: Location? = null

    /**
     * Set things up
     */
    override fun onCreate() {
        Log.d(TAG, "onCreate()")

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            maxWaitTime = MAX_WAIT_TIME
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                if (locationResult?.lastLocation != null) {
                    currentLocation = locationResult.lastLocation

                    // Notify our Activity that a new location was added. .
                    val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
                    intent.putExtra(EXTRA_LOCATION, currentLocation)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

                    // Updates notification content if this service is running as a foreground
                    // service.
                    if (serviceRunningInForeground) {
                        notificationManager.notify(
                            NOTIFICATION_ID,
                            generateNotification(currentLocation))
                    }
                } else {
                    Log.d(TAG, "Location missing in callback.")
                }
            }
        }
    }

    /**
     * If location tracking is canceled.
     *
     * @param intent The intent passed in.
     * @param flags Additional data about this start request.
     * @param startId A unique integer representing this specific request to start.
     * @return The return value indicates what semantics the system should use for the service's current started state.
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand()")

        val cancelLocationTrackingFromNotification =
            intent.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false)

        if (cancelLocationTrackingFromNotification) {
            unsubscribeToLocationUpdates()
            stopSelf()
        }
        // Tells the system not to recreate the service after it's been killed.
        return START_NOT_STICKY
    }


    /**
     * Client comes into foreground and binds to service, so the service can become a background services.
     *
     * @param intent The Intent that was used to bind to this service
     * @return Return an IBinder through which clients can call on to the service.
     */
    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind()")
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        return localBinder
    }

    /**
     * Client returns to the foreground and rebinds to service, so the service can become a background services.
     *
     * @param intent The Intent that was used to bind to this service
     */
    override fun onRebind(intent: Intent) {
        Log.d(TAG, "onRebind()")
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        super.onRebind(intent)
    }


    /**
     * Client leaves foreground, so service needs to become a foreground service to maintain the 'while-in-use' label.
     *
     * @param intent The Intent that was used to bind to this service
     * @return Return true if you would like to have the service's onRebind(Intent) method later called when new clients bind to it.
     */
    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "onUnbind()")
        if (!configurationChange && SharedPreferenceUtil.getLocationTrackingPref(this)) {
            Log.d(TAG, "Start foreground service")
            val notification = generateNotification(currentLocation)
            startForeground(NOTIFICATION_ID, notification)
            serviceRunningInForeground = true
        }
        return true
    }

    /**
     * Set variable to true if the configuration has changed
     *
     * @param newConfig The new configuration
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }

    /**
     * For client to subscribe to location updates
     */
    fun subscribeToLocationUpdates() {
        Log.d(TAG, "subscribeToLocationUpdates()")

        SharedPreferenceUtil.saveLocationTrackingPref(this, true)
        startService(Intent(applicationContext, ForegroundLocationService::class.java))

        try {
            Log.i(TAG, "request updates")
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper())
        } catch (unlikely: SecurityException) {
            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    /**
     * For client to unsubscribe location updates
     */
    fun unsubscribeToLocationUpdates() {
        Log.d(TAG, "unsubscribeToLocationUpdates()")

        try {
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Location Callback removed.")
                    stopSelf()
                } else {
                    Log.d(TAG, "Failed to remove Location Callback.")
                }
            }
            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
        } catch (unlikely: SecurityException) {
            SharedPreferenceUtil.saveLocationTrackingPref(this, true)
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    /**
     * Generates a notification that represent latest location.
     *
     * @param location The location just obtained
     * @return The generated notification
     */
    private fun generateNotification(location: Location?): Notification {
        Log.d(TAG, "generateNotification()")

        val mainNotificationText = location?.toText() ?: getString(R.string.no_location_text)
        val titleText = getString(R.string.app_name)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT)

            notificationManager.createNotificationChannel(notificationChannel)
        }

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(mainNotificationText)
            .setBigContentTitle(titleText)

        val launchActivityIntent = Intent(this, MainActivity::class.java)

        val cancelIntent = Intent(this, ForegroundLocationService::class.java)
        cancelIntent.putExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, true)

        val servicePendingIntent = PendingIntent.getService(
            this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val activityPendingIntent = PendingIntent.getActivity(
            this, 0, launchActivityIntent, 0)

        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(mainNotificationText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                R.drawable.ic_launch, getString(R.string.launch_activity),
                activityPendingIntent
            )
            .addAction(
                R.drawable.ic_cancel,
                getString(R.string.stop_location_updates_button_text),
                servicePendingIntent
            )
            .build()
    }

    /**
     * Class used for the client Binder.
     */
    inner class LocalBinder : Binder() {
        internal val service: ForegroundLocationService
            get() = this@ForegroundLocationService
    }

    /**
     * Static information
     */
    companion object {
        private const val TAG = "LocationService"

        private const val UPDATE_INTERVAL_IN_MILLISECONDS = 60000L;

        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

        private const val MAX_WAIT_TIME = 2000L;


        private const val PACKAGE_NAME = "com.example.onlySuggestion"

        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"

        private const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
            "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"

        private const val NOTIFICATION_ID = 12345678

        private const val NOTIFICATION_CHANNEL_ID = "only_suggestion_channel_01"
    }

}