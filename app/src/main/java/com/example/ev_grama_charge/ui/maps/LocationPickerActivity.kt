package com.example.ev_grama_charge.ui.maps

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationPickerActivity :
    AppCompatActivity(),
    OnMapReadyCallback {

    private lateinit var mMap:
            GoogleMap

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_location_picker
        )

        val mapFragment =
            supportFragmentManager
                .findFragmentById(
                    R.id.map
                ) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(
        googleMap: GoogleMap
    ) {

        mMap = googleMap

        val mysore =
            LatLng(
                12.2958,
                76.6394
            )

        mMap.moveCamera(
            CameraUpdateFactory
                .newLatLngZoom(
                    mysore,
                    13f
                )
        )

        mMap.setOnMapClickListener { latLng ->

            mMap.clear()

            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Selected Location")
            )

            val intent =
                Intent()

            intent.putExtra(
                "latitude",
                latLng.latitude
            )

            intent.putExtra(
                "longitude",
                latLng.longitude
            )

            setResult(
                RESULT_OK,
                intent
            )

            finish()
        }
    }
}