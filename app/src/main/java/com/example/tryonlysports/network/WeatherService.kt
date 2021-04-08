package com.example.tryonlysports.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/**
 * The weather data retrieval class
 *
 * @author Li Rui
 */

/**
 * The base url of data retrieval
 */
private const val BASE_URL = "https://api.data.gov.sg/v1/environment/"

/**
 * The moshi object for data parsing
 */
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

/**
 * The retrofit object for data retrieval
 */
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

/**
 * Data retrieval interface
 */
interface WeatherApiService {
    /**
     * Retrieve temperature and map it to Temperature Property
     */
    @GET("air-temperature")
    suspend fun getTemperature(): TemperatureProperty

    /**
     * Retrieve PM2.5 and map it to PM Property
     */
    @GET("pm25")
    suspend fun getPM25(): PMProperty

    /**
     * Retrieve forecast and map it to Forecast Property
     */
    @GET("2-hour-weather-forecast")
    suspend fun getForecast(): ForecastProperty

    /**
     * Retrieve UVI and map it to UVI Property
     */
    @GET("uv-index")
    suspend fun getUVI(): UVIProperty

}

/**
 * Weather service object creation
 */
object WeatherService {
    val retrofitService: WeatherApiService by lazy { retrofit.create(WeatherApiService::class.java) }
}
