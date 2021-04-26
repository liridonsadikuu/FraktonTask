package com.liridon.fraktontask

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.liridon.fraktontask.authentication.LoginActivity
import com.liridon.fraktontask.events.OpenFragmentEvent
import com.liridon.fraktontask.fragments.FavouritePlacesFragment
import com.liridon.fraktontask.fragments.MapFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkIfUserIsLoggedIn()
        onClickListeners()
        openFragment(MapFragment())

    }

    private fun openFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragmentHolder, fragment)
        transaction.commit()
    }

    private fun onClickListeners() {
        logout_text_view.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        tvMap.setOnClickListener {
            openFragment(MapFragment())
            mapIndicator.visibility = View.VISIBLE
            favPlacesIndicator.visibility = View.INVISIBLE
        }

        tvFavPlaces.setOnClickListener {
            openFragment(FavouritePlacesFragment())
            mapIndicator.visibility = View.INVISIBLE
            favPlacesIndicator.visibility = View.VISIBLE
        }

    }

    private fun checkIfUserIsLoggedIn() {
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
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



    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: OpenFragmentEvent){
        if (event.fragment is FavouritePlacesFragment) {
            tvFavPlaces.callOnClick()
        }else{
            tvMap.callOnClick()
        }


    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }




}