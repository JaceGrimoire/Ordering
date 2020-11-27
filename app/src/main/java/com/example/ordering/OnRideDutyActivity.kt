package com.example.ordering

import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

class OnRideDutyActivity : AppCompatActivity(), OnMapReadyCallback, TaskLoadedCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var txtUsername: TextView
    private lateinit var txtContactNo: TextView

    private lateinit var dest: LatLng
    private lateinit var origin: LatLng
    private lateinit var btnNext: Button

    // We won't allow back button, not unless it came from a specific button
    private var allowBack = false

    private var currentPolyline: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_ride_duty)

        txtUsername = findViewById(R.id.txt_ride_username)
        txtContactNo = findViewById(R.id.txt_ride_contact_no)

        btnNext = findViewById(R.id.btn_ride_finish)
        btnNext.setOnClickListener { userHasBeenDelivered() }


        dest = LatLng(
                intent.getDoubleExtra("extra_dest_lat", 0.0),
                intent.getDoubleExtra("extra_dest_long", 0.0)
        )
        origin = LatLng(
                intent.getDoubleExtra("extra_origin_lat", 0.0),
                intent.getDoubleExtra("extra_origin_long", 0.0)
        )

        Log.d(
                localClassName,
                "DestLatLng [lat=${dest.latitude}, long=${dest.longitude}] OriginLatLng [lat=${origin.latitude}, long=${origin.longitude}]"
        )

        txtUsername.text = intent.getStringExtra(BrowseUserRequests.EXTRA_USERNAME)
        // TODO: Include the mobile number, should it retrieve from the firebase?
//        txtContactNo.text = "Number"

        initializeMap()
    }

    private fun initializeMap() {
        val mapFragment =
                supportFragmentManager.findFragmentById(R.id.mapfragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun userHasBeenDelivered() {
        Log.i(localClassName, "User has been delivered")
        AlertDialog.Builder(this)
                .setTitle("Confirm your action")
                .setMessage("The user has been delivered. This action is irrevocable.")
                .setIcon(R.drawable.ic_baseline_confirmation_number_24)
                .setPositiveButton(
                        "Accept Request"
                ) { _, _ ->
                    // TODO: Update the client request as finished
                    // TODO: The client request should be removed from the BrowseUserRequests
                    // Go back to previous activity
                    allowBack = true
                    onBackPressed()
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
    }

    override fun onBackPressed() {
        if (allowBack) {
            super.onBackPressed()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
        mMap.addMarker(MarkerOptions().position(origin))

        mMap.addMarker(MarkerOptions().position(dest))
        mMap.addMarker(MarkerOptions().position(origin))

        // Receive directions
        val url = getUrl(origin, dest)
        FetchURL(this).execute(url, "driving")
    }

    private fun getUrl(origin: LatLng, dest: LatLng): String {
        val origin = "origin=${origin.latitude},${origin.longitude}"
        val destination = "destination=${dest.latitude},${dest.longitude}"
        val mode = "mode=driving"
        val parameters = "${origin}&${destination}&${mode}"
        val output = "json"
        val url = "https://maps.googleapis.com/maps/api/directions/${output}?${parameters}&key=${
            getString(R.string.google_maps_key)
        }"

        Log.d(localClassName, "apiUrl: $url")

        return url
    }

    /**
     * Draw the directions on top of the map fragment
     */
    override fun onTaskDone(vararg values: Any?) {
        currentPolyline?.let {
            currentPolyline!!.remove()
        }
        currentPolyline = mMap.addPolyline(values[0] as PolylineOptions)
    }
}
