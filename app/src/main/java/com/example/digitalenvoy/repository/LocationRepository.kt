package com.example.digitalenvoy.repository

import android.location.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Location?
}
