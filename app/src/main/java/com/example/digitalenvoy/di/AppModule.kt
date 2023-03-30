package com.example.digitalenvoy.di

import android.app.Application
import androidx.work.WorkManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /*@Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext appContext: Context): WeatherDatabase {
        return Room.databaseBuilder(
            appContext,
            WeatherDatabase::class.java,
            "WeatherDatabase"
        ).build()
    }*/

    @Singleton
    @Provides
    fun getWorkManagerInstance(application: Application): WorkManager {
        return  WorkManager.getInstance(application)
    }

    @Singleton
    @Provides
    fun getLocationClient(application: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(application)
    }

    @Singleton
    @Provides
    fun getSettingsClient(application: Application): SettingsClient {
        return LocationServices.getSettingsClient(application)
    }

}