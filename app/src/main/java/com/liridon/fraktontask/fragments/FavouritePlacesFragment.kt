package com.liridon.fraktontask.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.liridon.fraktontask.R
import com.liridon.fraktontask.events.PlaceEvent
import kotlinx.android.synthetic.main.fragment_favourite_places.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class FavouritePlacesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_places, container, false)
    }


    @Subscribe
    fun onEvent(event: PlaceEvent){

        tvCoordinate.text = "lati: " + event.latitude.toString() + " longi: " + event.longitude.toString()

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