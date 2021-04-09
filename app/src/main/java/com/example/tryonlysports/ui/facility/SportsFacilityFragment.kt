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
 * @author Wan Qian
 */
class SportsFacilityFragment : Fragment(), GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener,
GoogleMap.OnInfoWindowLongClickListener, GoogleMap.OnInfoWindowCloseListener{

    /**
     * Creates fragment portion of the view hierarchy that initiates the SportsFacilityViewModel
     *
     * @param inflater converts xml file fragment_sportsfacility into viewobjects
     * @param container a view that contains other views
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of MainActivity.
     * @return a view to display on the sports facility map page
     */


    private lateinit var sportsFacilityViewModel: SportsFacilityViewModel
    var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null

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

            /** Drops a marker at the specified point on the map */
            val sg = LatLng(1.3521, 103.8198)
            // For dropping a marker at a point on the Map

//            googleMap?.addMarker(
//                MarkerOptions().position(sg).title("Marker Title").snippet("Marker Description")
//            )

            /** Zooms in automatically to the position of the marker */
            val cameraPosition = CameraPosition.Builder().target(sg).zoom(12f).build()
            googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            addLayer()

        }
        return rootView
    }

    /** Passes the geoJson layer of sports facility onto the google map */
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

    inner class MyItem(
        lat: Double,
        lng: Double,
        title: String,
        snippet: String
    ) : ClusterItem {

        private val position: LatLng
        private val title: String
        private val snippet: String

        override fun getPosition(): LatLng {
            return position
        }

        override fun getTitle(): String? {
            return title
        }

        override fun getSnippet(): String? {
            return snippet
        }

        init {
            position = LatLng(lat, lng)
            this.title = title
            this.snippet = snippet
        }
    }


    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onInfoWindowClick(p0: Marker?) {
        TODO("Not yet implemented")
    }

    override fun onInfoWindowLongClick(p0: Marker?) {
        TODO("Not yet implemented")
    }

    override fun onInfoWindowClose(p0: Marker?) {
        TODO("Not yet implemented")
    }

//    internal inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
//        private val window: View = layoutInflater.inflate(R.layout.custom_info_window, null)
//        // These are both view groups containing an ImageView with id "badge" and two
//        // TextViews with id "title" and "snippet".
//        private val contents: View = layoutInflater.inflate(R.layout.custom_info_contents, null)
//
//        override fun getInfoWindow(marker: Marker): View? {
//            render(marker, window)
//            return window
//        }
//
//        override fun getInfoContents(marker: Marker): View? {
//
//            render(marker, contents)
//            return contents
//        }
//
//        private fun render(marker: Marker, view: View) {
//
//            // Set the title and snippet for the custom info window
//            val title: String? = marker.title
//            val titleUi = view.findViewById<TextView>(R.id.title)
//
//            if (title != null) {
//                // Spannable string allows us to edit the formatting of the text.
//                titleUi.text = SpannableString(title).apply {
//                    setSpan(ForegroundColorSpan(RED), 0, length, 0)
//                }
//            } else {
//                titleUi.text = ""
//            }
//
//            val snippet: String? = marker.snippet
//            val snippetUi = view.findViewById<TextView>(R.id.snippet)
//            if (snippet != null && snippet.length > 12) {
//                snippetUi.text = SpannableString(snippet).apply {
//                    setSpan(ForegroundColorSpan(MAGENTA), 0, 10, 0)
//                    setSpan(ForegroundColorSpan(BLUE), 12, snippet.length, 0)
//                }
//            } else {
//                snippetUi.text = ""
//            }
//        }
//
//}


}