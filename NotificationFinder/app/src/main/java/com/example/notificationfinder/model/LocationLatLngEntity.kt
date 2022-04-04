package com.example.notificationfinder.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


//parcelable -> data class를 넘겨줄 때 속도 향상
@Parcelize
data class LocationLatLngEntity (
     val latitude :Float,
     val longitude: Float
        ): Parcelable
