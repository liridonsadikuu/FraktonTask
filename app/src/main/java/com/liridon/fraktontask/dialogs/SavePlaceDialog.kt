package com.liridon.fraktontask.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import com.liridon.fraktontask.R
import com.liridon.fraktontask.events.InitTakePhotoEvent
import com.liridon.fraktontask.events.OpenFragmentEvent
import com.liridon.fraktontask.events.PlaceEvent
import com.liridon.fraktontask.fragments.FavouritePlacesFragment
import kotlinx.android.synthetic.main.dialog_save_place.*
import org.greenrobot.eventbus.EventBus


class SavePlaceDialog(context: Context): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_save_place)

        onClickListeners()

    }

    private fun onClickListeners() {
        btnNo.setOnClickListener {
            dismiss()
        }

        btn_yes.setOnClickListener {
            optionsLinearLayout.visibility = View.GONE
            btn_take_photo.visibility = View.VISIBLE
        }


        btn_take_photo.setOnClickListener {
            EventBus.getDefault().post(InitTakePhotoEvent())
            dismiss()
        }

    }



}