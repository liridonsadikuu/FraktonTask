package com.liridon.fraktontask.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.liridon.fraktontask.Place
import com.liridon.fraktontask.R
import com.liridon.fraktontask.adapters.FavPlacesAdapter
import com.liridon.fraktontask.events.PlaceEvent
import kotlinx.android.synthetic.main.fragment_favourite_places.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class FavouritePlacesFragment : Fragment() {

    private var favPlacesAdapter = FavPlacesAdapter(arrayListOf())

    companion object {
        val placesList: MutableList<Place> = mutableListOf()
    }


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
        favPlacesAdapter.setData(placesList)


    }


    @Subscribe
    fun onEvent(event: PlaceEvent){

        placesList.add(Place(event.latitude,event.longitude,event.takenPhoto))

        favPlacesAdapter.setData(placesList)

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