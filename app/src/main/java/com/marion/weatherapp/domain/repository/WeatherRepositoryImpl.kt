package com.marion.weatherapp.domain.repository

import com.marion.weatherapp.data.WeatherApi
import com.marion.weatherapp.domain.model.CurrentWeatherResponse
import com.marion.weatherapp.domain.model.WeatherForecastResponse
import com.marion.weatherapp.util.Resource
import com.marion.weatherapp.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val service: WeatherApi
) : WeatherRepository {

    override suspend fun getCurrentWeatherForCity(cityName: String): Flow<Resource<CurrentWeatherResponse>> =
        safeApiCall {
            service.getCurrentWeather(cityName)
        }

    override suspend fun get5DayForecast(cityName: String): Flow<Resource<WeatherForecastResponse>> =
        safeApiCall {
            service.get5DayForecast(cityName)
        }
}