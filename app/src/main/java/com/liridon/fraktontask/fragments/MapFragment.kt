package com.liridon.fraktontask.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.liridon.fraktontask.R
import com.liridon.fraktontask.dialogs.SavePlaceDialog
import java.text.DecimalFormat

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_map, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)





    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap

            mMap.setOnMapClickListener { LatLng->



                Handler(Looper.getMainLooper()).post {
                    val df = DecimalFormat()
                    df.setMaximumFractionDigits(3)
                    val lat: Double = df.format(LatLng.latitude).toDouble()
                    val lon: Double = df.format(LatLng.longitude).toDouble()
                    println("Lat and Longiii: " + lat + " " + lon)

                    if (context != null) {
                        val savePlaceDialog = SavePlaceDialog(context!!, lat, lon)
                        savePlaceDialog.show()
                    }


                }




            }
        }

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(
                MarkerOptions()
                        .position(sydney)
                        .title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }






}