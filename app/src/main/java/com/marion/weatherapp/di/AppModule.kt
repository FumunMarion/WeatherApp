package com.marion.weatherapp.di

import com.marion.weatherapp.data.WeatherApi
import com.marion.weatherapp.domain.repository.WeatherRepository
import com.marion.weatherapp.domain.repository.WeatherRepositoryImpl
import com.marion.weatherapp.domain.usecase.Fetch5DayForecastUseCase
import com.marion.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.marion.weatherapp.util.FIFTEEN_SECONDS_TIMEOUT
import com.marion.weatherapp.util.OPENWEATHERMAP_BASE_URL
import com.marion.weatherapp.util.TEN_SECONDS_TIMEOUT
import com.marion.weatherapp.util.TWENTY_SECONDS_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApi
    ): WeatherRepository =
        WeatherRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideRetrofitService(okHttpClient: OkHttpClient): WeatherApi {
        return Retrofit.Builder()
            .baseUrl(OPENWEATHERMAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGetCurrentWeatherUseCase(
        repository: WeatherRepository
    ): GetCurrentWeatherUseCase {
        return GetCurrentWeatherUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideFetch5DayForecastUseCase(
        repository: WeatherRepository
    ): Fetch5DayForecastUseCase {
        return Fetch5DayForecastUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TEN_SECONDS_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(FIFTEEN_SECONDS_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TEN_SECONDS_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(TWENTY_SECONDS_TIMEOUT, TimeUnit.SECONDS)
            .build()
}