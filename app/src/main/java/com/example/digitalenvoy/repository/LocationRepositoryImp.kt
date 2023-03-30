package com.example.digitalenvoy.repository

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@RequiresApi(Build.VERSION_CODES.M)
class LocationRepositoryImp @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val application: Application
) : LocationRepository {
    override suspend fun getCurrentLocation(): Location? {

        if (application.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_DENIED
        ) return null

        if (application.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_DENIED
        ) return null

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isGPSEnable =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )

        if(!isGPSEnable) return null

        return suspendCancellableCoroutine { cancellableContinuation ->
            fusedLocationProviderClient.lastLocation.apply {
                if(isComplete){
                    if(isSuccessful){
                        cancellableContinuation.resume(result)
                    }else{
                        cancellableContinuation.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }

                addOnCompleteListener {
                    cancellableContinuation.resume(result)
                }
                addOnCanceledListener {
                    cancellableContinuation.resume(null)
                }
                addOnFailureListener {
                    cancellableContinuation.cancel()
                }
            }
        }
    }
}