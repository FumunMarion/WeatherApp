package com.marion.weatherapp.domain.repository

import com.marion.weatherapp.domain.model.CurrentWeatherResponse
import com.marion.weatherapp.domain.model.WeatherForecastResponse
import com.marion.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getCurrentWeatherForCity(cityName: String): Flow<Resource<CurrentWeatherResponse>>

    suspend fun get5DayForecast(cityName: String): Flow<Resource<WeatherForecastResponse>>
}