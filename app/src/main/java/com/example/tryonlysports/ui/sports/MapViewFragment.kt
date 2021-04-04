package com.example.tryonlysports.ui.sports

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.tryonlysports.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapViewFragment : Fragment() {
    var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_doing_activity, container, false)
        mMapView = rootView.findViewById<View>(R.id.mapView) as MapView
        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume() // needed to get the map to display immediately
        Log.i("MapView", "onCreateViewCreated")
        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMapView!!.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(mMap: GoogleMap?) {
                googleMap = mMap

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(
                        (context as Activity?)!!,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        (context as Activity?)!!,
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
                googleMap?.setMyLocationEnabled(true)

                // For dropping a marker at a point on the Map
                val sydney = LatLng((-34).toDouble(), 151.0)
                googleMap?.addMarker(
                    MarkerOptions().position(sydney).title("Marker Title")
                        .snippet("Marker Description")
                )

                // For zooming automatically to the location of the marker
                val cameraPosition: CameraPosition =
                    CameraPosition.Builder().target(sydney).zoom(
                        12F
                    ).build()
                googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        })
        return rootView
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
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
}
