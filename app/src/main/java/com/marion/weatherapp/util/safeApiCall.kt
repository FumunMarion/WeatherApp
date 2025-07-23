package com.marion.weatherapp.util

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

inline fun <T : Any> safeApiCall(
    crossinline apiCall: suspend () -> Response<T>
): Flow<Resource<T>> = flow {
    emit(Resource.Loading())

    val response = apiCall()
    when {
        response.isSuccessful -> {
            val body = response.body()
            body?.let {
                emit(Resource.Success(body))
            } ?: run {
                emit(Resource.Error("Empty response body"))
                return@flow
            }
        }

        else -> {
            val errorMsg = response.errorBody()?.string() ?: "Unknown API error"
            Log.e("API_ERROR", "HTTP ${response.code()}: $errorMsg")
            emit(Resource.Error("Server error: $errorMsg"))
        }
    }
}.retryWhen { cause, attempt ->
    retryNetworkCallWithExponentialBackoff(cause, attempt)
}.catch { e ->
    if (e is CancellationException) throw e
    val message = when (e) {
        is IOException -> "Network error: ${e.message}"
        else -> "Unexpected error: ${e.message}"
    }
    Log.e("API_ERROR", "API call failed", e)
    emit(Resource.Error(message))
}.flowOn(Dispatchers.IO)