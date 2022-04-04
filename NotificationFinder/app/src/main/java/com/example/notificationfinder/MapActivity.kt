package com.example.notificationfinder

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.notificationfinder.Api_key.CAMERA_ZOOM_LEVEL
import com.example.notificationfinder.Api_key.PERMISSION_REQUEST_CODE
import com.example.notificationfinder.Api_key.SEARCH_RESULT_EXTRA_KEY
import com.example.notificationfinder.databinding.ActivityMapBinding
import com.example.notificationfinder.model.LocationLatLngEntity
import com.example.notificationfinder.model.SearchResultEntity
import com.example.notificationfinder.utils.RetrofitUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MapActivity : AppCompatActivity(), OnMapReadyCallback, CoroutineScope {

    private val binding by lazy{ ActivityMapBinding.inflate(layoutInflater)}

    private lateinit var searchResult: SearchResultEntity
    private lateinit var map: GoogleMap
    private lateinit var currentMarker : Marker
    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        job = Job()

        if(::searchResult.isInitialized.not()){
            intent?.let{
                searchResult = intent.getParcelableExtra(SEARCH_RESULT_EXTRA_KEY)
                    ?: throw Exception("데이터가 존재하지 않습니다.")
                setUpGoogleMap()
            }
        }
        bindViews()
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

    private fun bindViews(){
        binding.myLocationButton.setOnClickListener{
            getMyLocation()
        }
    }

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnabled) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    PERMISSION_REQUEST_CODE)
                } else{
                    setMyLocationListener()
                }
        }
    }
    @SuppressLint("MissingPermission")
    private fun setMyLocationListener(){
        val minTime = 1500L
        val minDistance = 100f

        if(::myLocationListener.isInitialized.not()){
            myLocationListener = MyLocationListener()
        }
        with(locationManager){
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }
    private fun onCurrentLocationChanged(locationEntity: LocationLatLngEntity){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
            LatLng(
                locationEntity.latitude.toDouble(),
                locationEntity.longitude.toDouble()),
            CAMERA_ZOOM_LEVEL
        ))
        loadReverseGeoInfo(locationEntity)
        removeLocationListener()
    }

    private fun loadReverseGeoInfo(locationLatLngEntity: LocationLatLngEntity){
        launch(coroutineContext){
            try{
                withContext(Dispatchers.IO){
                    val response = RetrofitUtil.apiService.getReverseGeoCode(
                        lat = locationLatLngEntity.latitude.toDouble(),
                        lon = locationLatLngEntity.longitude.toDouble()
                    )
                    if(response.isSuccessful){
                        val body = response.body()
                        withContext(Dispatchers.Main){
                            body?.let{
                                currentMarker = setUpMarker(
                                SearchResultEntity(
                                    fullAddress = it.addressInfo.fullAddress ?: "주소 정보 없음",
                                    buildingName = "내 위치",
                                    locationLatLng = locationLatLngEntity)
                                )
                                currentMarker?.showInfoWindow()
                            }
                        }
                    }
                }
            }catch(e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun removeLocationListener(){
        if(::locationManager.isInitialized && ::myLocationListener.isInitialized){
            locationManager.removeUpdates(myLocationListener) //
        }
    }
    inner class MyLocationListener: LocationListener {
        override fun onLocationChanged(location: Location) {
            val locationLatLngEntity = LocationLatLngEntity(
                location.latitude.toFloat(),
                location.longitude.toFloat()
            )
            onCurrentLocationChanged(locationLatLngEntity)
        }

    }
}