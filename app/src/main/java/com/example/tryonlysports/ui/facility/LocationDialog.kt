package com.example.tryonlysports.ui.facility

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipDescription
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.webkit.WebView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.tryonlysports.R
import com.google.maps.android.ktx.utils.heatmaps.heatmapTileProviderWithData

/**
 * This is the Fragment for the displaying Detailed facility information on sports facility google map
 *
 * @property description the string value containing the html format facility information to display.
 *
 * @author Li Rui
 */
class LocationDialogFragment(val description: String):DialogFragment(){
    /**
     * Retrieves the facility information and Creates the dialog to display the facility information.
     *
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of MainActivity.
     * @return a Dialog object to display.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view: View = inflater.inflate(R.layout.location_dialog_view, null)
            builder.setView(view)
                .setPositiveButton(R.string.ok, {dialog, id->})
            val webView: WebView = view.findViewById(R.id.webview)
            val mimeType = "text/html"
            val encoding = "UTF-8"
            webView.loadDataWithBaseURL("", description, mimeType, encoding, "")
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}