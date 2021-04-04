package com.example.tryonlysports.network

import com.example.tryonlysports.network.ForecastProperty
import com.example.tryonlysports.network.PMProperty
import com.example.tryonlysports.network.TemperatureProperty
import com.example.tryonlysports.network.UVIProperty
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

private const val BASE_URL = "https://api.data.gov.sg/v1/environment/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()


interface WeatherApiService {
    @GET("air-temperature")
    suspend fun getTemperature(): TemperatureProperty

    @GET("pm25")
    suspend fun getPM25(): PMProperty

    @GET("2-hour-weather-forecast")
    suspend fun getForecast(): ForecastProperty

    @GET("uv-index")
    suspend fun getUVI(): UVIProperty

}

object WeatherService {
    val retrofitService: WeatherApiService by lazy { retrofit.create(WeatherApiService::class.java) }
}
