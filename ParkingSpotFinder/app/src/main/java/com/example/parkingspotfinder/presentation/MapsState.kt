package com.example.parkingspotfinder.presentation

import com.example.parkingspotfinder.domain.model.ParkingSpot
import com.google.maps.android.compose.MapProperties

data class MapsState (
    val properties: MapProperties = MapProperties(),
    val isFalloutMap: Boolean = false,
    val parkingSpot: List<ParkingSpot> = emptyList()
)