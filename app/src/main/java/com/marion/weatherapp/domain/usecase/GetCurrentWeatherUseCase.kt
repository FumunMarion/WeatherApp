package com.marion.weatherapp.domain.usecase

import com.marion.weatherapp.domain.model.CurrentWeatherResponse
import com.marion.weatherapp.domain.repository.WeatherRepository
import com.marion.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String): Flow<Resource<CurrentWeatherResponse>> {
        return repository.getCurrentWeatherForCity(cityName)
    }
}