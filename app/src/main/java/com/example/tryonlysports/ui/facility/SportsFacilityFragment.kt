package com.example.tryonlysports.ui.facility

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Color.*
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.convertTo
import androidx.fragment.app.Fragment
import com.example.tryonlysports.MainActivity
import com.example.tryonlysports.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.GroundOverlayManager
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.collections.PolygonManager
import com.google.maps.android.collections.PolylineManager
import com.google.maps.android.data.Feature
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.kml.KmlLayer

import org.json.JSONObject

/**
 * This is the Fragment for the displaying sports facility on a google map
 *
 * @author Li Rui, Ye Ziyuan, Wan Qian
 */
class SportsFacilityFragment : Fragment(), GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener,
GoogleMap.OnInfoWindowLongClickListener, GoogleMap.OnInfoWindowCloseListener{

    /**
     * The SportsFacilityViewModel.
     */
    private lateinit var sportsFacilityViewModel: SportsFacilityViewModel

    /**
     * The MapView to display GoogleMap on sports facility fragment.
     */
    var mMapView: MapView? = null

    /**
     * The GoogleMap object to display the sports facility map.
     */
    private var googleMap: GoogleMap? = null

    /**
     * Creates fragment portion of the view hierarchy that initiates the SportsFacilityViewModel
     *
     * @param inflater converts xml file fragment_sportsfacility into viewobjects
     * @param container a view that contains other views
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of MainActivity.
     * @return a view to display on the sports facility map page
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_sportsfacility, container, false)
        mMapView = rootView.findViewById<View>(R.id.mapView) as MapView
        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume() // needed to get the map to display immediately
//        val activity : MainActivity = getActivity() as MainActivity
//        location = activity.currentLocation
        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMapView!!.getMapAsync { mMap ->
            googleMap = mMap

            // For showing a move to my location button
            if (ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this.requireContext(),
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

            }
            googleMap?.setMyLocationEnabled(true)
            googleMap?.setMinZoomPreference(10.0f)
            googleMap?.setMaxZoomPreference(13.0f)

            val sg = LatLng(1.3521, 103.8198)


            val cameraPosition = CameraPosition.Builder().target(sg).zoom(12f).build()
            googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            addLayer()

        }
        return rootView
    }

    /**
     * Adds a GeoJson layer onto the sports facility map.
     *
     */
    fun addLayer() {
        super.onStart()
        val geoJsonLineLayer = GeoJsonLayer(googleMap, R.raw.sgsportfacilities, context )
        geoJsonLineLayer.addLayerToMap()
        geoJsonLineLayer.setOnFeatureClickListener { feature ->
            val description = feature.getProperty("Description")
            val newFragment = LocationDialogFragment(description)
            Log.i("Hello??", requireActivity().toString())
            newFragment.show(requireFragmentManager(), "missiles")
        }

    }

    /**
     * Performs actions once the function is clicked.
     *
     * @param p0 the marker object that has been clicked.
     * @return boolean indicating whether the marker is clicked.
     */
    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Performs actions once the info window is clicked.
     *
     * @param p0 the marker object which generates the info window.
     */
    override fun onInfoWindowClick(p0: Marker?) {
        TODO("Not yet implemented")
    }

    /**
     * Performs actions once the info window is long clicked.
     *
     * @param p0 the marker object which generates the info window.
     */
    override fun onInfoWindowLongClick(p0: Marker?) {
        TODO("Not yet implemented")
    }

    /**
     * Performs actions once the info window is closed.
     *
     * @param p0 the marker object which generates the info window.
     */
    override fun onInfoWindowClose(p0: Marker?) {
        TODO("Not yet implemented")
    }



}
