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


class DoingSportsFragment : Fragment(),OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {
    var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null

    private var gpsTrack: Polyline? = null
    private val mapFragment: SupportMapFragment? = null
    private var googleApiClient: GoogleApiClient? = null
    private var lastKnownLatLng: LatLng? = null
    private lateinit var location : Location
    private lateinit var viewModel: DoingSportsViewModel
    private lateinit var binding: FragmentDoingActivityBinding
    private var type: String = ""

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

        binding.terminateButton.setOnClickListener {terminate() }
        return binding.root
    }

    private fun terminate() {
        stopLocationUpdates()
        this.findNavController().navigate(DoingSportsFragmentDirections.actionDoingActivityToSportsResultFragment(type,
            viewModel.passedTimeValue.value!!, viewModel.totalDistanceValue.value!!.toFloat()
        ))
    }

    override fun onStart() {
        googleApiClient!!.connect()
        super.onStart()
    }

    override fun onStop() {
        googleApiClient!!.disconnect()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        if (googleApiClient?.isConnected() == true) {
            startLocationUpdates()
        }
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        mMapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }

    override fun onConnected(@Nullable bundle: Bundle?) {
        startLocationUpdates()
    }

    override fun onConnectionSuspended(i: Int) {}

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

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
    protected fun stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
            googleApiClient, this
        )
    }

    private fun updateTrack() {
        val points = gpsTrack!!.points
        points.add(lastKnownLatLng!!)
        gpsTrack!!.points = points

    }

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