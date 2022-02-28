package com.example.parkingspotfinder.data

import com.example.parkingspotfinder.domain.model.ParkingSpot

fun ParkingSpotEntity.toParkingSpot() : ParkingSpot{
    return ParkingSpot(
        lat =lat,
        lng = lng,
        id= id
    )
}
fun ParkingSpot.toParkingSpotEntity() : ParkingSpotEntity{
    return ParkingSpotEntity(
        lat = lat,
        lng = lng,
        id= id
    )
}
