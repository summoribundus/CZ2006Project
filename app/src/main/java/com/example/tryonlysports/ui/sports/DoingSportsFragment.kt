package com.example.tryonlysports.ui.sports

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tryonlysports.MainActivity
import com.example.tryonlysports.R
import com.example.tryonlysports.databinding.FragmentDoingActivityBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

/**
 * This is the Fragment for the doing sports function.
 *
 * @author Ye Ziyuan
 */
class DoingSportsFragment : Fragment(),OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {

    /**
     * The MapView to display GoogleMap on doing sports fragment.
     */
    var mMapView: MapView? = null

    /**
     * The GoogleMap object to display the route and location.
     */
    private var googleMap: GoogleMap? = null

    /**
     * The polyline as the user workout route.
     */
    private var gpsTrack: Polyline? = null

    /**
     * The map Fragment.
     */
    private val mapFragment: SupportMapFragment? = null

    /**
     * The Google API client to handle the GoogleMap activities.
     */
    private var googleApiClient: GoogleApiClient? = null

    /**
     * The last known latitude and longtitude to update to the user track polyline.
     */
    private var lastKnownLatLng: LatLng? = null

    /**
     * The location obtained by Android OS.
     */
    private lateinit var location : Location

    /**
     * The DoingSportsViewModel.
     */
    private lateinit var viewModel: DoingSportsViewModel

    /**
     * The DoingSports data binding.
     */
    private lateinit var binding: FragmentDoingActivityBinding

    /**
     * the workout type. (cycling/walking/jogging)
     */
    private var type: String = ""

    /**
     * Creates the fragment's portion of the view hierarchy and initializes Map view and GoogleMap API client.
     *
     * @param inflater converts the xml file fragment_doing_sports into View objects.
     * @param container a special view to contain other views (e.g. MapView).
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of MainActivity.
     * @return a View to display on the Doing Sports page.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_doing_activity, container, false
        )
        val arguments = DoingSportsFragmentArgs.fromBundle(requireArguments())
        val activity : MainActivity = getActivity() as MainActivity
        location = activity.currentLocation
        val viewModelFactory = DoingSportsViewModelFactory(arguments.type)
        type = arguments.type
        Log.i("DoingSports", arguments.type)
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            DoingSportsViewModel::class.java
        )
        binding.doingSportsViewModel = viewModel

        mMapView = binding.mapView
        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume() // needed to get the map to display immediately
        Log.i("MapView", "onCreateViewCreated")
        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMapView!!.getMapAsync(this)
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder((context as Activity?)!!)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        }
        binding.typeText.text =  type

        binding.terminateButton.setOnClickListener {terminate() }
        return binding.root
    }

    /**
     * Terminates the Map tracking and navigates to the result summary page.
     *
     */
    private fun terminate() {
        stopLocationUpdates()
        this.findNavController().navigate(DoingSportsFragmentDirections.actionDoingActivityToSportsResultFragment(type,
            viewModel.passedTimeValue.value!!, viewModel.totalDistanceValue.value!!.toFloat()
        ))
    }

    /**
     * Connects to Google API Client once the fragment starts.
     *
     */
    override fun onStart() {
        googleApiClient!!.connect()
        super.onStart()
    }

    /**
     * Disconnects the Google API Client once the fragment stops.
     *
     */
    override fun onStop() {
        googleApiClient!!.disconnect()
        super.onStop()
    }

    /**
     * Continues the map tracking once the fragment resumes.
     *
     */
    override fun onResume() {
        super.onResume()
        if (googleApiClient?.isConnected() == true) {
            startLocationUpdates()
        }
        mMapView?.onResume()
    }

    /**
     * Pauses the Map tracking once the fragment pauses.
     *
     */
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        mMapView?.onPause()
    }

    /**
     * Destroy the Map tracking  once the fragment is destroyed
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    /**
     * Operates when the fragment is on low memory.
     *
     */
    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }

    /**
     * Starts location updates once the fragment is connected to the location service.
     *
     * @param bundle passes data between fragments and activities.
     */
    override fun onConnected(@Nullable bundle: Bundle?) {
        startLocationUpdates()
    }

    /**
     * Operates when the connection is suspended.
     *
     * @param i passed to the location service for checking.
     */
    override fun onConnectionSuspended(i: Int) {}

    /**
     * Operates when the connection is failed.
     *
     * @param connectionResult the result of connection.
     */
    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    /**
     * Updates and display Map tracking once the location of the user changed.
     *
     * @param location the current location of the user fetched by Android OS.
     */
    override fun onLocationChanged(location: Location) {
        lastKnownLatLng = LatLng(location.getLatitude(), location.getLongitude())
        updateTrack()
        viewModel.updateSpeed(location)
        viewModel.updateTimePassed()
        gpsTrack?.let { viewModel.updateRouteLength(it) }
        binding.speedText.text = viewModel.speed.value
        binding.timePassedText.text = viewModel.timePassed.value
        binding.distanceText.text =  viewModel.totalDistance.value
        // For zooming automatically to the location of the marker
        // For dropping a marker at a point on the Map
        val latlngdata = LatLng(location.latitude, location.longitude)

        googleMap?.setMinZoomPreference(15.0f)

        // For zooming automatically to the location of the marker
        val cameraPosition: CameraPosition =
            CameraPosition.Builder().target(latlngdata).zoom(
                12F
            ).build()
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    /**
     * Starts location updates and connects to location service.
     *
     */
    protected fun startLocationUpdates() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            googleApiClient,
            locationRequest,
            this
        )
    }

    /**
     * Stops location updates.
     *
     */
    protected fun stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
            googleApiClient, this
        )
    }

    /**
     * Draws updates track on map.
     *
     */
    private fun updateTrack() {
        val points = gpsTrack!!.points
        points.add(lastKnownLatLng!!)
        gpsTrack!!.points = points

    }

    /**
     * Prepares the map and tracking route once the fragment is ready.
     *
     * @param p0 the GoogleMap object to update the track.
     */
    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0

        val polylineOptions = PolylineOptions()
        polylineOptions.color(Color.BLACK)
        polylineOptions.width(4f)
        gpsTrack = googleMap?.addPolyline(polylineOptions)


        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(
                (context as Activity?)!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                (context as Activity?)!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        googleMap?.setMyLocationEnabled(true)

        // For dropping a marker at a point on the Map
        val latlngdata = LatLng(location.latitude, location.longitude)
        Log.i("DoingSports", location.latitude.toString())
        Log.i("DoingSports", location.longitude.toString())

        googleMap?.setMinZoomPreference(15.0f)

        // For zooming automatically to the location of the marker
        val cameraPosition: CameraPosition =
            CameraPosition.Builder().target(latlngdata).zoom(
                12F
            ).build()
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

}