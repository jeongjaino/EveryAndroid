package com.example.notificationfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notificationfinder.Api_key.CAMERA_ZOOM_LEVEL
import com.example.notificationfinder.Api_key.SEARCH_RESULT_EXTRA_KEY
import com.example.notificationfinder.databinding.ActivityMapBinding
import com.example.notificationfinder.model.SearchResultEntity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy{ ActivityMapBinding.inflate(layoutInflater)}

    private lateinit var searchResult: SearchResultEntity
    private lateinit var map: GoogleMap
    private lateinit var currentMarker : Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(::searchResult.isInitialized.not()){
            intent?.let{
                searchResult = intent.getParcelableExtra(SEARCH_RESULT_EXTRA_KEY)
                    ?: throw Exception("데이터가 존재하지 않습니다.")
                setUpGoogleMap()
            }
        }

    }

    private fun setUpGoogleMap(){
        val mapFragment = (supportFragmentManager.findFragmentById(R.id.mapFragment)
                as SupportMapFragment?)?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        currentMarker = setUpMarker(searchResult)
        currentMarker.showInfoWindow()
    }
    private fun setUpMarker(searchResult: SearchResultEntity): Marker{
        val positionLatLng = LatLng(
            searchResult.locationLatLng.latitude.toDouble(),
            searchResult.locationLatLng.longitude.toDouble()
        )
        val markerOptions = MarkerOptions().apply{
            position(positionLatLng)
            title(searchResult.buildingName)
            snippet(searchResult.fullAddress)
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(positionLatLng, CAMERA_ZOOM_LEVEL))
        return map.addMarker(markerOptions)!!
    }
}