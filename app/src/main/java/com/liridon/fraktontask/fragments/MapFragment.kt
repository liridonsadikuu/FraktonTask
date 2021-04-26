package com.liridon.fraktontask.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.liridon.fraktontask.R
import com.liridon.fraktontask.dialogs.SavePlaceDialog
import com.liridon.fraktontask.events.InitTakePhotoEvent
import com.liridon.fraktontask.events.LocateOnMapEvent
import com.liridon.fraktontask.events.OpenFragmentEvent
import com.liridon.fraktontask.events.PlaceEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.DecimalFormat


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val CAMERA_REQUEST_CODE = 1
    private val PERMISSION_REQUEST_CODE_CAMERA = 2
    private val REQUEST_PERMISSION_CODE_NEVER_ASK_AGAIN_SETTING  = 3
    var latitude: Double = 0.0
    var longitude: Double = 0.0

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
                    latitude = df.format(LatLng.latitude).toDouble()
                    longitude = df.format(LatLng.longitude).toDouble()
                    println("Lat and Longiii: " + latitude + " " + longitude)

                    if (context != null) {
                        val savePlaceDialog = SavePlaceDialog(context!!)
                        savePlaceDialog.show()
                    }
                }
            }
        }

        // Add a marker in Sydney and move the camera
        val prishtina = LatLng(42.6629138, 21.1655028)
        mMap.addMarker(
                MarkerOptions()
                        .position(prishtina)
                        .title("Marker in Prishtina"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(prishtina, 8f))
    }

    @Subscribe
    fun onEvent(event: InitTakePhotoEvent){
        if (checkPermission()) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context,"Can not open camera!", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission();
        }
    }

    @Subscribe
    fun onEvent(event: LocateOnMapEvent){
      if (::mMap.isInitialized){
          mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(event.latitude,event.longitude), 8f))
      }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap

            EventBus.getDefault().post(OpenFragmentEvent(FavouritePlacesFragment()))

            Handler().postDelayed({
                EventBus.getDefault().post(PlaceEvent(latitude,longitude,imageBitmap))
            }, 300)
        }else if (requestCode == REQUEST_PERMISSION_CODE_NEVER_ASK_AGAIN_SETTING){
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context,"Can not open camera!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun checkPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                !== PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            false
        } else true
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(
                Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE_CAMERA)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE_CAMERA -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context,"Can not open camera!", Toast.LENGTH_SHORT).show()
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.CAMERA)) {
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("Warning")
                            builder.setMessage("You need to allow camera permission in order to take photo!")
                            builder.setIcon(android.R.drawable.ic_dialog_alert)
                            builder.setPositiveButton("Ok"){dialogInterface, which ->
                                requestPermission()
                            }
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()

                        }else{
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("Warning")
                            builder.setMessage("You have disabled camera permission, please go to app settings in order to allow ")
                            builder.setIcon(android.R.drawable.ic_dialog_alert)
                            builder.setPositiveButton("Open app settings"){dialogInterface, which ->
                                val intent =
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri: Uri = Uri.fromParts("package", activity!!.getPackageName(), null)
                                intent.data = uri
                                startActivityForResult(intent, REQUEST_PERMISSION_CODE_NEVER_ASK_AGAIN_SETTING)
                            }
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }

                    }
                }
            }
        }
    }




}