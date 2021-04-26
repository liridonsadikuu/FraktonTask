package com.liridon.fraktontask.adapters

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liridon.fraktontask.R
import com.liridon.fraktontask.events.LocateOnMapEvent
import com.liridon.fraktontask.events.OpenFragmentEvent
import com.liridon.fraktontask.events.ShowAlertOnItemLongClickEvent
import com.liridon.fraktontask.fragments.MapFragment
import com.liridon.fraktontask.model.Place
import kotlinx.android.synthetic.main.fav_places_item.view.*
import org.greenrobot.eventbus.EventBus


class FavPlacesAdapter(var list: ArrayList<Place>) : RecyclerView.Adapter<FavPlacesAdapter.FavPlacesViewHolder>() {

    fun setData(newList: List<Place>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavPlacesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_places_item, parent, false)
        return FavPlacesViewHolder(view)
    }

    override fun getItemCount() = list.size

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FavPlacesViewHolder, position: Int) {

        val item = list[position]
        holder.bind(item, position)
    }

    class FavPlacesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Place, position: Int) {
            itemView.tvLat.text = "Latitude: " + item.latitude.toString()
            itemView.tvLong.text = "Longitude: " + item.longitude.toString()
            itemView.ivPhotoTaken.setImageBitmap(item.photo)

            itemView.item_holder.setOnClickListener {
                if (item.latitude != null && item.longitude != null) {
                    EventBus.getDefault().post(OpenFragmentEvent(MapFragment()))
                    Handler().postDelayed({
                        EventBus.getDefault().post(LocateOnMapEvent(item.latitude!!, item.longitude!!))
                    }, 300)
                }
            }

            itemView.item_holder.setOnLongClickListener() {
                EventBus.getDefault().post(ShowAlertOnItemLongClickEvent(item,position))
                true
            }

        }

    }
}