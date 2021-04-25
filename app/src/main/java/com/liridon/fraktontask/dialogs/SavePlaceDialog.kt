package com.liridon.fraktontask.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.Window
import com.liridon.fraktontask.R
import com.liridon.fraktontask.events.OpenFragmentEvent
import com.liridon.fraktontask.events.PlaceEvent
import com.liridon.fraktontask.fragments.FavouritePlacesFragment
import kotlinx.android.synthetic.main.dialog_save_place.*
import org.greenrobot.eventbus.EventBus


class SavePlaceDialog(context: Context,var latitude: Double,var longitude: Double): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_save_place)

        onClickListeners()

    }

    private fun onClickListeners() {
        btn_cancel.setOnClickListener {
            dismiss()
        }

        btn_save.setOnClickListener {

            EventBus.getDefault().post(OpenFragmentEvent(FavouritePlacesFragment()))

            Handler().postDelayed({
                EventBus.getDefault().post(PlaceEvent(latitude,longitude))
            }, 1000)
             dismiss()

        }

    }



}