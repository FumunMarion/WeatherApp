package com.marion.weatherapp.domain.model

data class WeatherForecastResponse(
    val city: City,
    val list: List<WeatherData>
)

data class City(
    val name: String,
    val country: String
)

data class WeatherData(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)

data class Temp(
    val day: Double,
    val min: Double,
    val max: Double
)

data class WeatherCondition(
    val description: String,
    val icon: String // Weather icon
)
