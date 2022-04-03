package com.example.notificationfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notificationfinder.databinding.ActivityMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy{ ActivityMapBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpGoogleMap()
        setContentView(binding.root)
    }

    private fun setUpGoogleMap(){
        val mapFragment = (supportFragmentManager.findFragmentById(R.id.mapFragment)
                as SupportMapFragment?)?.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {

    }
}