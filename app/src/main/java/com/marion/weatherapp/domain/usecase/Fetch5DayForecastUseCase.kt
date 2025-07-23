package com.marion.weatherapp.domain.usecase

import com.marion.weatherapp.domain.model.WeatherForecastResponse
import com.marion.weatherapp.domain.repository.WeatherRepository
import com.marion.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Fetch5DayForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String): Flow<Resource<WeatherForecastResponse>> {
        return weatherRepository.get5DayForecast(cityName)
    }
}
