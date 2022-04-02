package com.example.notificationfinder.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


//parcelable -> data class를 넘겨줄 때 속도 향상
@Parcelize
data class LocationLatLngEntitiy (
    private val latitude :Float,
    private val longitude: Float
        ): Parcelable
