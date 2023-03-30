package com.example.digitalenvoy.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.digitalenvoy.repository.LocationRepository
import com.example.digitalenvoy.util.Resource
import com.example.digitalenvoy.workers.LocationWorker
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val workManager: WorkManager,
    private val settingsClient: SettingsClient
) : ViewModel() {

    companion object {
        const val LOCATION_WORK_TAG = "LOCATION_WORK_TAG"
    }

    /**
     * Task #2 - Actions: Scheduling work
     * Initiate a work manager periodically and buildconfig will decide either to run it on 20min or 60min
     * Using WorkManager for recurring task for data persistence upon reboot and app kill.
     */

    private fun trackLocationWithWorkManager() {
        val repeatInterval : Int = com.example.digitalenvoy.BuildConfig.PERIODIC_INTERVAL
        val locationWorker = PeriodicWorkRequestBuilder<LocationWorker>(
            repeatInterval.toLong(), TimeUnit.MINUTES)
            .addTag(LOCATION_WORK_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            LOCATION_WORK_TAG, ExistingPeriodicWorkPolicy.UPDATE, locationWorker
        )
    }

    fun stopTrackLocation() {
        workManager.cancelAllWorkByTag(LOCATION_WORK_TAG)
    }

    //checking if gps is on or not
    fun locationSetup() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 3 * 1000
            fastestInterval = 5 * 1000
        }
        settingsClient.checkLocationSettings(
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                .setAlwaysShow(true).build()
        ).addOnSuccessListener {
            trackLocationWithWorkManager()
        }.addOnFailureListener {
            Log.e("Activity", "Gps not enabled")
        }
    }
}