package com.marion.weatherapp.data

import com.marion.weatherapp.BuildConfig
import com.marion.weatherapp.domain.CurrentWeatherResponse
import com.marion.weatherapp.domain.WeatherForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = BuildConfig.OPENWEATHERMAP_API_KEY,
        @Query("units") units: String = "metric"
    ): Response<CurrentWeatherResponse>

    @GET("forecast")
    suspend fun get5DayForecast(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = BuildConfig.OPENWEATHERMAP_API_KEY,
        @Query("units") units: String = "metric" // Metric system
    ): Response<WeatherForecastResponse>
}