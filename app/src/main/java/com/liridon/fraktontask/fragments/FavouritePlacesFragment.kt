package com.liridon.fraktontask.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.liridon.fraktontask.db.PlaceDatabase
import com.liridon.fraktontask.R
import com.liridon.fraktontask.adapters.FavPlacesAdapter
import com.liridon.fraktontask.events.PlaceEvent
import com.liridon.fraktontask.events.ShowAlertOnItemLongClickEvent
import com.liridon.fraktontask.model.Place
import kotlinx.android.synthetic.main.fragment_favourite_places.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class FavouritePlacesFragment : Fragment() {

    private var favPlacesAdapter = FavPlacesAdapter(arrayListOf())

    var placesList: MutableList<Place> = mutableListOf()

    lateinit var db: PlaceDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite_places, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rvFavPlaces.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favPlacesAdapter
        }

        db = Room.databaseBuilder(
                    context!!,
                    PlaceDatabase::class.java, "place_db.db").build()

        GlobalScope.launch {
            placesList = db.getPlaceDao().getAllPlaces().toMutableList()
            withContext(Dispatchers.Main) {
                if (placesList.size>0) {
                    favPlacesAdapter.setData(placesList)
                    tvNoPlacesSaved.visibility = View.GONE
                }else{
                    tvNoPlacesSaved.visibility = View.VISIBLE
                }
            }
        }
    }

    @Subscribe
    fun onEvent(event: PlaceEvent){
        GlobalScope.launch {
            db.getPlaceDao().insert(Place(null,event.latitude,event.longitude,event.takenPhoto))
            placesList = db.getPlaceDao().getAllPlaces().toMutableList()
            withContext(Dispatchers.Main) {
                favPlacesAdapter.setData(placesList)
                tvNoPlacesSaved.visibility = View.GONE
            }
        }
    }

    @Subscribe
    fun onEvent(event: ShowAlertOnItemLongClickEvent){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert")
        builder.setMessage("Do you want to delete this item?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            favPlacesAdapter.removeItem(event.position)

            GlobalScope.launch {
                placesList = db.getPlaceDao().getAllPlaces().toMutableList()
                db.getPlaceDao().deletePlace(placesList.get(event.position))
            }
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

}