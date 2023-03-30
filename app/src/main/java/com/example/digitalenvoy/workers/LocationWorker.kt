package com.example.digitalenvoy.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.digitalenvoy.repository.LocationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import javax.inject.Inject
//Task #3 - Recurring work implementation
/**
 * The only issue that we have come across is the case where some Chinese OEMs treat swipe to dismiss from Recents as a force stop. When that happens, WorkManager will reschedule all pending jobs, next time the app starts up. Given that this is a CDD violation, there is not much more that WorkManager can do given its a client library. source
 */
/**
 * Using Fuse location service because it is efficient way of collecting location data in background recommend by google docs.
 */
@HiltWorker
class LocationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val locationRepository: LocationRepository
) : CoroutineWorker(appContext, workerParams) {

    private val MAX_ATTEMPT = 3

    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel()
            }
            val location1 = locationRepository.getCurrentLocation()
            val location2 = locationRepository.getCurrentLocation()
            val location3 = locationRepository.getCurrentLocation()
            if (location1 == null && location2 ==null && location3 == null) {
                if (runAttemptCount < MAX_ATTEMPT) { // max_attempt = 3
                    Result.retry()
                } else {
                    Result.failure()
                }
            } else {

                val locationToToast =
                    "Location1 = lat:${location1?.latitude}, Long:${location1?.longitude} \n" +
                            "location2 = lat:${location2?.latitude}, Long:${location2?.longitude} \n" +
                            "location3 = lat:${location3?.latitude}, Long:${location3?.longitude}"

                ContextCompat.getMainExecutor(applicationContext).execute {
                    Toast.makeText(applicationContext, locationToToast, Toast.LENGTH_LONG).show()
                    //TODO Task #4 - Local storage options
                    // we could save the location data to room db and or we could store the data in cloud by making a API
                }

                val notification = notificationBuilder.build()
                notificationManager.notify(1001, notification)
                Log.d(LocationWorker::class.simpleName, locationToToast)
                Result.success()
            }
        } catch (error: Throwable) {
            Log.e(LocationWorker::class.simpleName, error.toString())
            Result.failure()
        }
    }

    private val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        .setContentTitle(TITLE)
        .setTicker(TITLE)
        .setOngoing(true)
        .setContentText("Location tracking")
        .setSmallIcon(androidx.core.R.drawable.notification_icon_background)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, TITLE, importance)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "location_channel"
        const val TITLE = "Location"
    }
}
