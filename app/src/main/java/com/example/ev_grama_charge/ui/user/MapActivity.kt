package com.example.ev_grama_charge.ui.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.ui.booking.BookingActivity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class MapActivity :
    AppCompatActivity(),
    OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var bottomSheet:
            LinearLayout

    private lateinit var sheetBehavior:
            BottomSheetBehavior<LinearLayout>

    private lateinit var tvHostName:
            TextView

    private lateinit var tvBadge:
            TextView

    private lateinit var tvPrice:
            TextView

    private lateinit var tvDistance:
            TextView

    private lateinit var tvRating:
            TextView

    private lateinit var btnBook:
            MaterialButton

    private lateinit var btnNavigate:
            MaterialButton

    private lateinit var btnClose:
            ImageView

    private lateinit var btnFilter:
            FloatingActionButton

    private var userLocation:
            Location? = null

    private val hostList =
        mutableListOf<HostData>()

    private var bestHost:
            HostData? = null

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_map
        )

        val mapFragment =
            supportFragmentManager
                .findFragmentById(
                    R.id.map
                ) as SupportMapFragment

        mapFragment.getMapAsync(this)

        getUserLocation()

        bottomSheet =
            findViewById(R.id.bottomSheet)

        sheetBehavior =
            BottomSheetBehavior
                .from(bottomSheet)

        sheetBehavior.state =
            BottomSheetBehavior.STATE_HIDDEN

        sheetBehavior.isHideable = true

        bottomSheet.visibility =
            View.GONE

        tvHostName =
            findViewById(R.id.tvHostName)

        tvBadge =
            findViewById(R.id.tvBadge)

        tvPrice =
            findViewById(R.id.tvPrice)

        tvDistance =
            findViewById(R.id.tvDistance)

        tvRating =
            findViewById(R.id.tvRating)

        btnBook =
            findViewById(R.id.btnBook)

        btnNavigate =
            findViewById(R.id.btnNavigate)

        btnClose =
            findViewById(R.id.btnClose)

        btnFilter =
            findViewById(R.id.btnFilter)

        btnClose.setOnClickListener {

            bottomSheet.visibility =
                View.GONE

            sheetBehavior.state =
                BottomSheetBehavior.STATE_HIDDEN
        }

        setupFilterMenu()
    }

    override fun onMapReady(
        googleMap: GoogleMap
    ) {

        mMap = googleMap

        enableLocation()

        val mysore =
            LatLng(
                12.2958,
                76.6394
            )

        mMap.moveCamera(

            CameraUpdateFactory
                .newLatLngZoom(
                    mysore,
                    12f
                )
        )

        loadHosts()
    }

    private fun getUserLocation() {

        val fusedLocationClient =
            LocationServices
                .getFusedLocationProviderClient(this)

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener {

                userLocation = it
            }
    }

    private fun setupFilterMenu() {

        btnFilter.setOnClickListener {

            val popup =
                PopupMenu(this, btnFilter)

            popup.menu.add("All")
            popup.menu.add("Available")
            popup.menu.add("Nearest")
            popup.menu.add("Cheapest")

            popup.setOnMenuItemClickListener {

                when (it.title.toString()) {

                    "All" -> {

                        showMarkers(hostList)
                    }

                    "Available" -> {

                        showMarkers(

                            hostList.filter { host ->
                                host.available
                            }
                        )
                    }

                    "Nearest" -> {

                        showMarkers(

                            hostList.sortedBy { host ->
                                host.distance
                            }
                        )
                    }

                    "Cheapest" -> {

                        showMarkers(

                            hostList.sortedBy { host ->
                                host.price
                            }
                        )
                    }
                }

                true
            }

            popup.show()
        }
    }

    private fun loadHosts() {

        FirebaseFirestore.getInstance()
            .collection("hosts")
            .get()

            .addOnSuccessListener { result ->

                hostList.clear()

                for (doc in result.documents) {

                    val lat =

                        doc.getString(
                            "profileLatitude"
                        )?.toDoubleOrNull()

                            ?:

                            (doc["lat"] as? Number)
                                ?.toDouble()

                            ?: continue

                    val lng =

                        doc.getString(
                            "profileLongitude"
                        )?.toDoubleOrNull()

                            ?:

                            (doc["lng"] as? Number)
                                ?.toDouble()

                            ?: continue

                    val price =
                        (doc["price"] as? Number)
                            ?.toDouble()
                            ?: 120.0

                    val rating =
                        (doc["rating"] as? Number)
                            ?.toDouble()
                            ?: 4.5

                    val name =

                        doc.getString(
                            "shopName"
                        )

                            ?:

                            doc.getString(
                                "name"
                            )

                            ?: "Charging Station"

                    val address =
                        doc.getString(
                            "address"
                        ) ?: ""

                    val available =
                        doc.getBoolean(
                            "available"
                        ) ?: true

                    val distance =
                        userLocation?.let {

                            val results =
                                FloatArray(1)

                            Location.distanceBetween(
                                it.latitude,
                                it.longitude,
                                lat,
                                lng,
                                results
                            )

                            results[0] / 1000.0

                        } ?: 0.0

                    hostList.add(

                        HostData(
                            name,
                            address,
                            price,
                            rating,
                            distance,
                            available,
                            lat,
                            lng
                        )
                    )
                }

                bestHost =
                    hostList.minByOrNull {

                        it.price +
                                it.distance -
                                (it.rating * 2)
                    }

                showMarkers(hostList)

                setupMarkerClick()
            }
    }

    private fun showMarkers(
        list: List<HostData>
    ) {

        mMap.clear()

        list.forEach { host ->

            val marker =
                mMap.addMarker(

                    MarkerOptions()

                        .position(
                            LatLng(
                                host.lat,
                                host.lng
                            )
                        )

                        .title(host.name)

                        .snippet(host.address)

                        .icon(

                            when {

                                host == bestHost -> {

                                    BitmapDescriptorFactory
                                        .defaultMarker(
                                            BitmapDescriptorFactory.HUE_AZURE
                                        )
                                }

                                host.available -> {

                                    BitmapDescriptorFactory
                                        .defaultMarker(
                                            BitmapDescriptorFactory.HUE_GREEN
                                        )
                                }

                                else -> {

                                    BitmapDescriptorFactory
                                        .defaultMarker(
                                            BitmapDescriptorFactory.HUE_RED
                                        )
                                }
                            }
                        )
                )

            marker?.tag = host
        }
    }

    private fun setupMarkerClick() {

        mMap.setOnMarkerClickListener { marker ->

            val data =
                marker.tag as? HostData
                    ?: return@setOnMarkerClickListener false

            tvHostName.text =
                data.name

            tvPrice.text =
                getString(
                    R.string.price_format,
                    data.price
                )

            tvDistance.text =
                getString(
                    R.string.distance_format,
                    data.distance
                )

            tvRating.text =
                getString(
                    R.string.rating_format,
                    data.rating
                )

            tvBadge.visibility =
                if (data == bestHost)
                    View.VISIBLE
                else
                    View.GONE

            btnBook.setOnClickListener {

                startActivity(

                    Intent(
                        this,
                        BookingActivity::class.java
                    )
                        .putExtra(
                            "name",
                            data.name
                        )
                        .putExtra(
                            "price",
                            data.price
                        )
                )

                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
            }

            btnNavigate.setOnClickListener {

                val uri =
                    "google.navigation:q=${data.lat},${data.lng}"
                        .toUri()

                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        uri
                    )
                )
            }

            bottomSheet.visibility =
                View.VISIBLE

            sheetBehavior.state =
                BottomSheetBehavior.STATE_EXPANDED

            true
        }
    }

    private fun enableLocation() {

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        mMap.isMyLocationEnabled = true
    }

    override fun finish() {

        super.finish()

        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }

    data class HostData(

        val name: String,

        val address: String,

        val price: Double,

        val rating: Double,

        val distance: Double,

        val available: Boolean,

        val lat: Double,

        val lng: Double
    )
}