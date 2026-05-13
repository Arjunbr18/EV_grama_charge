package com.example.ev_grama_charge.ui.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ev_grama_charge.R
import com.example.ev_grama_charge.data.model.Host
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class HostListActivity : AppCompatActivity() {

    private lateinit var recyclerView:
            RecyclerView

    private lateinit var adapter:
            HostAdapter

    private val hostList =
        mutableListOf<Host>()

    private val db =
        FirebaseFirestore.getInstance()

    private var listenerRegistration:
            ListenerRegistration? = null

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_host_list
        )

        recyclerView =
            findViewById(R.id.recyclerHosts)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        adapter =
            HostAdapter(hostList) { host ->

                val intent =
                    Intent(
                        this,
                        HostDetailsActivity::class.java
                    )

                intent.putExtra(
                    "hostId",
                    host.hostId
                )

                intent.putExtra(
                    "name",
                    host.name
                )

                intent.putExtra(
                    "price",
                    host.price
                )

                intent.putExtra(
                    "rating",
                    host.rating
                )

                intent.putExtra(
                    "distance",
                    host.distance
                )

                intent.putExtra(
                    "available",
                    host.isAvailable
                )

                startActivity(intent)

                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
            }

        recyclerView.adapter =
            adapter

        loadHostsRealtime()
    }

    private fun loadHostsRealtime() {

        listenerRegistration =

            db.collection("hosts")

                .addSnapshotListener { value, error ->

                    if (
                        error != null ||
                        value == null
                    ) {
                        return@addSnapshotListener
                    }

                    hostList.clear()

                    for (document in value.documents) {

                        // SUPPORT BOTH OLD + NEW HOSTS

                        val name =

                            document.getString(
                                "shopName"
                            )

                                ?:

                                document.getString(
                                    "name"
                                )

                                ?: "Charging Station"

                        val latitude =

                            document.getString(
                                "profileLatitude"
                            )?.toDoubleOrNull()

                                ?:

                                (document["lat"] as? Number)
                                    ?.toDouble()

                                ?: 0.0

                        val longitude =

                            document.getString(
                                "profileLongitude"
                            )?.toDoubleOrNull()

                                ?:

                                (document["lng"] as? Number)
                                    ?.toDouble()

                                ?: 0.0

                        val price =

                            (document["price"] as? Number)
                                ?.toDouble()

                                ?: 120.0

                        val rating =

                            (document["rating"] as? Number)
                                ?.toDouble()

                                ?: 4.5

                        val available =

                            document.getBoolean(
                                "available"
                            ) ?: true

                        val host = Host(

                            hostId = document.id,

                            name = name,

                            latitude = latitude,

                            longitude = longitude,

                            price = price,

                            rating = rating,

                            isAvailable = available
                        )

                        hostList.add(host)
                    }

                    calculateDistances()
                }
    }

    private fun calculateDistances() {

        val fusedLocationClient =

            LocationServices
                .getFusedLocationProviderClient(this)

        if (

            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(

                this,

                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),

                100
            )

            return
        }

        fusedLocationClient.lastLocation

            .addOnSuccessListener { location ->

                if (location != null) {

                    for (host in hostList) {

                        val results =
                            FloatArray(1)

                        Location.distanceBetween(

                            location.latitude,
                            location.longitude,

                            host.latitude,
                            host.longitude,

                            results
                        )

                        host.distance =
                            (results[0] / 1000)
                                .toDouble()
                    }

                    hostList.sortBy {
                        it.distance
                    }

                    adapter.notifyDataSetChanged()
                }
            }
    }

    override fun onDestroy() {

        super.onDestroy()

        listenerRegistration?.remove()
    }

    override fun finish() {

        super.finish()

        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }
}