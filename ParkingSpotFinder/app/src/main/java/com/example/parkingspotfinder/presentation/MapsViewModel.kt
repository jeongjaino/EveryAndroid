package com.example.parkingspotfinder.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingspotfinder.domain.model.ParkingSpot
import com.example.parkingspotfinder.domain.repository.ParkingSpotRepository
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: ParkingSpotRepository
) : ViewModel() {

    var state by mutableStateOf(MapsState())

    init{
        viewModelScope.launch {
            repository.getParkingSpot().collectLatest { spots->
                state = state.copy(
                    parkingSpot = spots
                )
            }
        }
    }

    fun onEvent(event: MapEvent){
        when(event){
            is MapEvent.ToggleFalloutMap-> {
                state = state.copy(
                    properties =  state.properties.copy(
                        mapStyleOptions = if(state.isFalloutMap){
                            null
                        }else{
                            MapStyleOptions(MapStyle.json)
                        }
                    ),
                    isFalloutMap = !state.isFalloutMap
                )
            }
            is MapEvent.OnMapLongClick ->{
                viewModelScope.launch {
                    repository.insertParkingSpot(
                        ParkingSpot(
                            event.latLng.latitude,
                            event.latLng.longitude
                        )
                    )
                }
            }
            is MapEvent.OnInfoWindowLongClick ->{
                viewModelScope.launch {
                    repository.deleteParkingSpot(event.spot)
                }
            }
        }
    }
}
