package com.liridon.fraktontask.adapters

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liridon.fraktontask.model.Place
import com.liridon.fraktontask.R
import com.liridon.fraktontask.events.LocateOnMapEvent
import com.liridon.fraktontask.events.OpenFragmentEvent
import com.liridon.fraktontask.fragments.MapFragment
import kotlinx.android.synthetic.main.fav_places_item.view.*
import org.greenrobot.eventbus.EventBus


class FavPlacesAdapter(var list: ArrayList<Place>) : RecyclerView.Adapter<FavPlacesAdapter.MenuListViewHolder>() {

    fun setData(lista: List<Place>) {
        list.clear()
        list.addAll(lista)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_places_item, parent, false)
        return MenuListViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {

        val item = list[position]
        holder.bind(item, position)
    }

    class MenuListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Place, position: Int) {
            itemView.tvLat.text = "Latitude: " + item.latitude.toString()
            itemView.tvLong.text = "Longitude: " + item.longitude.toString()
           // itemView.ivPhotoTaken.setImageBitmap(item.photo)

            itemView.item_holder.setOnClickListener {
                if (item.latitude != null && item.longitude != null) {
                    EventBus.getDefault().post(OpenFragmentEvent(MapFragment()))
                    Handler().postDelayed({
                        EventBus.getDefault().post(LocateOnMapEvent(item.latitude!!, item.longitude!!))
                    }, 300)
                }
            }

        }

    }
}