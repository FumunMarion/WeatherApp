package com.marion.weatherapp.util

import kotlinx.coroutines.delay
import okio.IOException
import kotlin.math.pow

const val OPENWEATHERMAP_BASE_URL = "https://api.openweathermap.org/data/2.5/"

suspend fun retryNetworkCallWithExponentialBackoff(
    cause: Throwable,
    attempt: Long,
): Boolean {
    return if (attempt < 3 && cause is IOException) {
        val delayDuration = (1000L * (2.0.pow(attempt.toDouble()))).toLong()
        delay(delayDuration)
        true
    } else {
        false
    }
}