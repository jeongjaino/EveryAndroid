package com.example.parkingspotfinder.data

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity
data class ParkingSpotEntity (
    val lat: Double,
    val lng: Double,
    @PrimaryKey val id :Int? = null
    )