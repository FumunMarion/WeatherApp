package com.marion.weatherapp.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.marion.weatherapp.domain.model.WeatherData
import com.marion.weatherapp.domain.model.WeatherForecastResponse
import com.marion.weatherapp.util.Resource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {

    val forecastState by viewModel.fiveDayForecastState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = White,
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 12.dp, shape = RoundedCornerShape(4.dp)),
                title = {
                    Text(
                        text = "Weather App", color = Black,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                )
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = forecastState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(1000)) togetherWith fadeOut(
                        animationSpec = tween(
                            2500
                        )
                    )
                }) { state ->
                when (state) {
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is Resource.Success -> {
                        val forecast = state.data
                        forecast?.let {
                            ForecastContent(it)
                        } ?: run {
                            ErrorContent(message = "Empty response body")

                        }

                    }

                    is Resource.Error -> {
                        ErrorContent(
                            message = state.message ?: "An error occurred"
                        )

                    }

                    else -> {}
                }
            }
        }
    }

}

@Composable
fun ForecastContent(forecast: WeatherForecastResponse) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .fillMaxHeight()
    ) {
        // Hourly Forecast Section
        Text(
            text = "Today's 3-Hour forecast",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.92f),
            color = Black
        )
        Box(
            Modifier
                .fillMaxWidth()
                .background(color = White, shape = RoundedCornerShape(20.dp))
                .padding(12.dp), contentAlignment = Alignment.Center
        ) {
            HourlyForecast(forecast.list.take(8))
        }

        Spacer(modifier = Modifier.height(40.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            border = BorderStroke(width = 0.5.dp, color = LightGray)
        ) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // Daily Forecast Section
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = null,
                        tint = Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "5-day forecast",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Black,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                DailyForecast(
                    forecast.list.chunked(8).take(5)
                )
            }

        }

    }
}

@Composable
fun HourlyForecast(hourlyData: List<WeatherData>) {
    LazyRow(
        state = rememberLazyListState(),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(hourlyData, key = { it.dt }) { weather ->
            HourlyForecastCard(weather)
        }
    }
}

@Composable
fun HourlyForecastCard(weatherData: WeatherData) {
    val time = remember(weatherData.dt) {
        SimpleDateFormat("h a", Locale.getDefault())
            .format(Date(weatherData.dt * 1000))
    }
    val roundedTemp = weatherData.main.temp.roundToInt()

    Column(
        modifier = Modifier
            .width(85.dp)
            .height(180.dp)
            .padding(6.dp)
            .background(shape = CircleShape, color = Black.copy(0.2f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "${roundedTemp}°", fontSize = 15.sp, color = Black)
        Spacer(modifier = Modifier.height(12.dp))
        Image(
            painter = rememberAsyncImagePainter(
                model = "https://openweathermap.org/img/wn/${weatherData.weather.firstOrNull()?.icon}.png"
            ),
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )
        Text(
            text = time,
            fontSize = 12.sp,
            color = Black,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "${weatherData.main.humidity}%",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Black.copy(0.6f),
        )
    }
}

@Composable
fun DailyForecast(dailyData: List<List<WeatherData>>) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(dailyData, key = { it.first().dt }) { dayData ->
            DailyForecastCard(dayData)
        }
    }
}

@Composable
fun DailyForecastCard(dayData: List<WeatherData>) {
    val date = remember(dayData.first().dt) {
        SimpleDateFormat("EEE", Locale.getDefault())
            .format(Date(dayData.first().dt * 1000))
    }

    val roundedMaxTemp = dayData.maxOf { it.main.temp_max }.roundToInt()
    val roundedMinTemp = dayData.minOf { it.main.temp_min }.roundToInt()
    val weatherIcon = dayData.first().weather.firstOrNull()?.icon
    val precipitationProbability = dayData.sumOf { it.main.humidity } / dayData.size

    Column(
        modifier = Modifier
            .width(100.dp)
            .height(190.dp)
            .padding(6.dp)
            .background(shape = CircleShape, color = LightGray.copy(0.1f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontSize = 13.sp,
                        color = Black
                    )
                ) {
                    append("${roundedMaxTemp}° /")
                }
                withStyle(
                    SpanStyle(
                        fontSize = 13.sp,
                        color = LightGray
                    )
                ) {
                    append(" ${roundedMinTemp}°")
                }
            }
        )
        Text(
            text = "${precipitationProbability}%",
            fontSize = 12.sp,
            color = Blue
        )

        Image(
            painter = rememberAsyncImagePainter(
                model = "https://openweathermap.org/img/wn/$weatherIcon.png"
            ),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = date,
            fontSize = 12.sp,
            color = Black
        )
    }
}

@Composable
fun ErrorContent(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error loading forecast: $message",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = White
        )
    }
}

