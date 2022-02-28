package com.example.parkingspotfinder.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.parkingspotfinder.data.ParkingSpotDatabase
import com.example.parkingspotfinder.data.ParkingSpotRepositoryImpl
import com.example.parkingspotfinder.domain.repository.ParkingSpotRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideParkingSpotDatabase(app: Application): ParkingSpotDatabase{
        return Room.databaseBuilder(
            app,
            ParkingSpotDatabase::class.java,
            "parking_spots_db"
        ).build()
    }
    @Singleton
    @Provides
    fun provideParkingSpotRepository(db: ParkingSpotDatabase): ParkingSpotRepository{
        return ParkingSpotRepositoryImpl(db.dao)
    }
}