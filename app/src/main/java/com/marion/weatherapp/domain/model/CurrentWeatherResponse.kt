package com.marion.weatherapp.domain.model

data class CurrentWeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val sys: Sys,
    val name: String,
    val timezone: Int,
)


data class Weather(
    val description: String,
    val main: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double
)

data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)