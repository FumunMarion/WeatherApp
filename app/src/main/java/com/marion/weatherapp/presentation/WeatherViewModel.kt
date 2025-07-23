package com.marion.weatherapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marion.weatherapp.domain.model.CurrentWeatherResponse
import com.marion.weatherapp.domain.model.WeatherForecastResponse
import com.marion.weatherapp.domain.usecase.Fetch5DayForecastUseCase
import com.marion.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.marion.weatherapp.util.Resource
import com.marion.weatherapp.util.Resource.Error
import com.marion.weatherapp.util.Resource.Idle
import com.marion.weatherapp.util.Resource.Loading
import com.marion.weatherapp.util.Resource.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getFiveDayForecastUseCase: Fetch5DayForecastUseCase
) : ViewModel() {

    init {
        getFiveDayForecast(city = "Miami")
    }

    private val _currentWeatherState = MutableStateFlow<Resource<CurrentWeatherResponse>>(Idle())
    val currentWeatherState = _currentWeatherState.asStateFlow()

    private val _fiveDayForecastState = MutableStateFlow<Resource<WeatherForecastResponse>>(Idle())
    val fiveDayForecastState = _fiveDayForecastState.asStateFlow()

    fun getFiveDayForecast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getFiveDayForecastUseCase(city).collect { forecastResult ->
                when (forecastResult) {
                    is Loading -> {
                        _fiveDayForecastState.value = Loading()
                    }

                    is Success -> {
                        _fiveDayForecastState.value = forecastResult.data?.let {
                            Success(it)
                        } ?: Error("No data received")
                    }

                    is Error -> {
                        _fiveDayForecastState.value =
                            Error(forecastResult.message ?: "Unknown error")
                    }

                    else -> {
                        _fiveDayForecastState.value = Idle()
                    }
                }
            }
        }
    }

    fun getCurrentWeather(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentWeatherUseCase(city).collect { weatherResult ->
                when (weatherResult) {
                    is Loading -> {
                        _currentWeatherState.value = Loading()
                    }

                    is Success -> {
                        _currentWeatherState.value = weatherResult.data?.let {
                            Success(it)
                        } ?: Error("No data received")
                    }

                    is Error -> {
                        _currentWeatherState.value = Error(weatherResult.message ?: "Unknown error")
                    }

                    else -> {
                        _currentWeatherState.value = Idle()
                    }
                }
            }
        }
    }

}