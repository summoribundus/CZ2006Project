package com.example.tryonlysports.ui.weather

import android.app.Application
import android.graphics.drawable.Drawable
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tryonlysports.R
import com.example.tryonlysports.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * The TAG used for debugging.
 */
private const val TAG = "WeatherViewModel"

/**
 * Weather view model class.
 *
 * This class containing data and processing logic for weather view.
 *
 * @property application The application who creating this viewmodel.
 * @property location Location of the user.
 * @author Li Rui
 */
class WeatherViewModel(application: Application, val location: Location) : AndroidViewModel(application) {
    /**
     * The enumeration class representation the weather condition
     */
    enum class WeatherCondition {
        THUNDERY_SHOWER,
        SHOWER,
        WINDY,
        CLOUDY_NIGHT,
        PARTLY_CLOUDY,
        CLOUDY,
        FAIR,
        HAZY,
        OVERCAST,
        UNKNOWN
    }

    /**
     * The current temperature as mutable live private data
     */
    private val _temperature = MutableLiveData<Double>()

    /**
     * The current temperature as public data that cannot be modified.
     */
    val temperature: LiveData<Double> get() = _temperature

    /**
     * The current PM2.5 value as mutable live private data
     */
    private val _pm25 = MutableLiveData<Double>()
    /**
     * The current PM2.5 value as public data that cannot be modified.
     */
    val pm25 : LiveData<Double> get() = _pm25

    /**
     * The current UV index as mutable live private data
     */
    private val _uvi = MutableLiveData<Double>()
    /**
     * The current UV index as public data that cannot be modified.
     */
    val uvi : LiveData<Double> get() = _uvi

    /**
     * The current weather forecast as mutable live private data
     */
    private val _forecast = MutableLiveData<String>()
    /**
     * The current weather forecast as public data that cannot be modified.
     */
    val forecast: LiveData<String> get() = _forecast

    /**
     * The failure message from Internet as mutable live private data
     */
    private val _response = MutableLiveData<String>()
    /**
     * The failure message from Internet as public data that cannot be modified.
     */
    val response : LiveData<String> get() = _response

    /**
     * The current temperature as mutable live private data
     */
    private val _sunProtectionSuggestion = MutableLiveData<String>()
    /**
     * The current temperature as public data that cannot be modified.
     */
    val sunProtectionSuggestion : LiveData<String> get() = _sunProtectionSuggestion

    /**
     * The outdoor activity suggestion as mutable live private data
     */
    private val _outdoorActivitySuggestion = MutableLiveData<String>()
    /**
     * The outdoor activity suggestion as public data that cannot be modified.
     */
    val outdoorActivitySuggestion : LiveData<String> get() = _outdoorActivitySuggestion

    /**
     * The activity duration suggestion as mutable live private data
     */
    private val _activityDurationSuggestion = MutableLiveData<String>()
    /**
     * The activity duration suggestion as public data that cannot be modified.
     */
    val activityDurationSuggestion : LiveData<String> get() = _activityDurationSuggestion

    /**
     * The heat warning suggestion as mutable live private data
     */
    private val _heatWarningSuggestion = MutableLiveData<String>()
    /**
     * The heat warning suggestion as public data that cannot be modified.
     */
    val heatWarningSuggestion : LiveData<String> get() = _heatWarningSuggestion

    /**
     * The weather icon to display given weather forecast as mutable live private data
     */
    private val _weatherIcon = MutableLiveData<Drawable>()
    /**
     * The weather icon to display given weather forecast as public data that cannot be modified.
     */
    val weatherIcon : LiveData<Drawable> get() = _weatherIcon

    /**
     * Earth radius
     */
    private val Radius = 6371

    /**
     * The current weather condition
     */
    private lateinit var weatherCondition : WeatherCondition

    /**
     * The method called at the view model creation time
     */
    init {
        weatherProperty()
    }

    /**
     * Start the function for API data fetch from main thread as coroutine
     */
    private fun weatherProperty() {
        viewModelScope.launch (Dispatchers.Main) {
            getForecastProperty()
            getTemperatureProperty()
            getPMProperty()
            getUVIProperty()
        }
    }

    /**
     * The temperature property fetched back from temperature API
     */
    private lateinit var temperatureProperty: TemperatureProperty

    /**
     * The PM2.5 property fetched back from PM2.5 API
     */
    private lateinit var pmProperty: PMProperty

    /**
     * The forecast property fetched back from weather forecast API
     */
    private lateinit var forecastProperty: ForecastProperty

    /**
     * The UV index property fetched back from UV index API
     */
    private lateinit var uviProperty: UVIProperty

    /**
     * Fetch a weather forecast as forecast property in a difference thread.
     * Await for return and then map property to weather condition. Find the weather icon and produce outdoor activity suggestion accordingly.
     */
    private suspend fun getForecastProperty() {
        val value = viewModelScope.async {
            try {
                forecastProperty = WeatherService.retrofitService.getForecast()
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
        value.await()
        Log.i(TAG, "Forecast fetched")

        val forecastLocationName = getForecastLocationName(location.latitude, location.longitude, forecastProperty.area_metadata)

        val forecastMap = forecastProperty.items[0].forecasts.associateBy ({it.area}, {it.forecast})
        _forecast.value = forecastMap[forecastLocationName]

        mapToWeatherCondition()
        getWeatherIcon()
        getOutdoorActivitySuggestion()
    }

    /**
     * Fetch temperature information as temperature property in a difference thread.
     * Await for return and then find the temperature of the nearest temperature station . Produce outdoor activity suggestion accordingly.
     */
    private suspend fun getTemperatureProperty() {
        val value = viewModelScope.async {
            try {
                temperatureProperty = WeatherService.retrofitService.getTemperature()
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
        value.await()
        Log.i(TAG, "Temperature fetched");
        val tempStationId = getTempStationId(location.latitude, location.longitude, temperatureProperty.metadata.stations)
        val tempMap = temperatureProperty.items[0].readings.associateBy ( {it.station_id}, {it.value} )
        _temperature.value = tempMap[tempStationId]

        getHeatWarningSuggestion()
    }

    /**
     * Fetch PM2.5 information as pmproperty in a difference thread.
     * Await for return and then find the PM2.5 of nearest region . Produce activity duration suggestion accordingly.
     */
    private suspend fun getPMProperty() {
        val value = viewModelScope.async {
            try {
                pmProperty = WeatherService.retrofitService.getPM25()
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
        value.await()
        Log.i(TAG, "PM fetched");
        val pm25Region = getPM25Region(location.latitude, location.longitude, pmProperty.region_metadata)

        if (pm25Region == "east") _pm25.value = pmProperty.items[0].readings.pm25_one_hourly.east
        else if (pm25Region == "west") _pm25.value = pmProperty.items[0].readings.pm25_one_hourly.west
        else if (pm25Region == "central") _pm25.value = pmProperty.items[0].readings.pm25_one_hourly.central
        else if (pm25Region == "south") _pm25.value = pmProperty.items[0].readings.pm25_one_hourly.south
        else _pm25.value = pmProperty.items[0].readings.pm25_one_hourly.north

        getActivityDurationSuggestion()
    }

    /**
     * Fetch UV index as uviproperty in a difference thread.
     * Await for return and then find the uv index of nearest timestamp . Produce sun protection suggestion accordingly.
     */
    private suspend fun getUVIProperty() {
        val value = viewModelScope.async {
            try {
                uviProperty = WeatherService.retrofitService.getUVI()
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
        value.await()
        Log.i(TAG, "UVI fetched");
        _uvi.value = uviProperty.items[0].index[0].value

        getSunProtectionSuggestion()
    }

    /**
     * Map weather forecast string to weather condition
     */
    private fun mapToWeatherCondition() {
        if (_forecast.value!!.contains("fair", true))
            weatherCondition = WeatherCondition.FAIR
        else if (_forecast.value!!.contains("cloudy", true)) {
            if (_forecast.value!!.contains("night", true))
                weatherCondition = WeatherCondition.CLOUDY_NIGHT
            else if (_forecast.value!!.contains("partly", true))
                weatherCondition = WeatherCondition.PARTLY_CLOUDY
            else
                weatherCondition = WeatherCondition.CLOUDY
        }
        else if (_forecast.value!!.contains("windy", true))
            weatherCondition = WeatherCondition.WINDY
        else if (_forecast.value!!.contains("showers", true)) {
            if (_forecast.value!!.contains("thundery", true))
                weatherCondition = WeatherCondition.THUNDERY_SHOWER
            else weatherCondition = WeatherCondition.SHOWER
        }
        else if (_forecast.value!!.contains("rain", true)) {
            weatherCondition = WeatherCondition.SHOWER
        }
        else if (_forecast.value!!.contains("hazy", true))
            weatherCondition = WeatherCondition.HAZY
        else if (_forecast.value!!.contains("overcast", true))
            weatherCondition = WeatherCondition.OVERCAST
        else weatherCondition = WeatherCondition.UNKNOWN
    }

    /**
     * Using weather condition to find the most suitable weather icon
     */
    private fun getWeatherIcon() {
        val context = getApplication<Application>().applicationContext
        when (weatherCondition) {
            WeatherCondition.THUNDERY_SHOWER->
                _weatherIcon.value = ContextCompat.getDrawable(context,
                        R.drawable.ic_cloud_rain_thunderstorm_lightning_storm)
            WeatherCondition.SHOWER->
                _weatherIcon.value = ContextCompat.getDrawable(context, R.drawable.ic_loud_rain)
            WeatherCondition.WINDY->
                _weatherIcon.value = ContextCompat.getDrawable(context, R.drawable.ic_weather_wind)
            WeatherCondition.PARTLY_CLOUDY->
                _weatherIcon.value = ContextCompat.getDrawable(context, R.drawable.ic_cloud_sun)
            WeatherCondition.CLOUDY->
                _weatherIcon.value = ContextCompat.getDrawable(context, R.drawable.ic_cloudiness)
            WeatherCondition.FAIR->
                _weatherIcon.value = ContextCompat.getDrawable(context, R.drawable.ic_sun)
            WeatherCondition.HAZY->
                _weatherIcon.value = ContextCompat.getDrawable(context, R.drawable.ic_haze)
            WeatherCondition.OVERCAST->
                _weatherIcon.value = ContextCompat.getDrawable(context, R.drawable.ic_overcast)
            WeatherCondition.CLOUDY_NIGHT->
                _weatherIcon.value = ContextCompat.getDrawable(context, R.drawable.ic_clouds_cloudiness_moon_night_sky)
            WeatherCondition.UNKNOWN->
                _weatherIcon.value = ContextCompat.getDrawable(context, R.drawable.ic_unknown)
        }
    }

    /**
     * Produce sun protection suggestion based on uv index
     */
    private fun getSunProtectionSuggestion() {
        if (_uvi.value!! < 0.0) {
            _sunProtectionSuggestion.value = "Invalid value."
        } else if (_uvi.value!! in 0.0..0.9) {
            _sunProtectionSuggestion.value = "No precaution needed."
        } else if (_uvi.value!! in 1.0..2.0) {
            _sunProtectionSuggestion.value = "Wear sunglasses on bright days."
        } else if (_uvi.value!! in 3.0..5.0) {
            _sunProtectionSuggestion.value = "Covering up and using sunscreen. "
        } else if (_uvi.value!! in 6.0..7.0) {
            _sunProtectionSuggestion.value = "Reduce outdoor activities at noon."
        } else if (_uvi.value!! in 8.0..10.0) {
            _sunProtectionSuggestion.value = "Extra precautions needed. "
        } else {
            _sunProtectionSuggestion.value = "Take all precautions. "
        }
    }

    /**
     * Produce outdoor activity suggestion based on weather condition.
     */
    private fun getOutdoorActivitySuggestion() {
        when (weatherCondition) {
            WeatherCondition.FAIR -> _outdoorActivitySuggestion.value = "Nice day for outdoor activities."
            WeatherCondition.CLOUDY_NIGHT, WeatherCondition.PARTLY_CLOUDY, WeatherCondition.CLOUDY, WeatherCondition.WINDY ->
                _outdoorActivitySuggestion.value = "Suitable for outdoor activities."
            WeatherCondition.SHOWER, WeatherCondition.THUNDERY_SHOWER, WeatherCondition.HAZY, WeatherCondition.OVERCAST ->
                _outdoorActivitySuggestion.value = "Recommend to stay indoors."
            WeatherCondition.UNKNOWN -> _outdoorActivitySuggestion.value = "Unable to give suggestions."
        }
    }

    /**
     * Produce activity duration suggestion based on pm2.5
     */
    private fun getActivityDurationSuggestion() {
        if (_pm25.value!! < 0.0) {
            _activityDurationSuggestion.value = "Invalid value."
        } else if (_pm25.value!! in 0.0..55.0) {
            _activityDurationSuggestion.value = "Continue with normal activities."
        } else if (_pm25.value!! in 56.0..150.0) {
            _activityDurationSuggestion.value = "Reduce strenuous outdoor activity."
        } else if (_pm25.value!! in 151.0..250.0) {
            _activityDurationSuggestion.value = "Avoid strenuous outdoor activity."
        } else {
            _activityDurationSuggestion.value = "Minimize all outdoor activity."
        }
    }

    /**
     * Produce heat warning suggestion based on temperature
     */
    private fun getHeatWarningSuggestion() {
        if (_temperature.value!! in 27.0..32.0) {
            _heatWarningSuggestion.value = "Fatigue possible with prolonged \nactivity."
        } else if (_temperature.value!! in 33.0..40.0) {
            _heatWarningSuggestion.value = "Heatstroke possible with prolonged \nactivity."
        } else {
            _heatWarningSuggestion.value = "Not specific suggestion"
        }
    }

    /**
     * Find out the forecast location name based on user's current location
     */
    /**
     * Find out the forecast location name based on user's current location
     *
     * @param latitude User latitude
     * @param longitude User longitude
     * @param areaMetadata List of forecast locational information
     * @return nearest location name
     */
    private fun getForecastLocationName(
        latitude: Double,
        longitude: Double,
        areaMetadata: List<ForecastMetadataProperty>
    ): String {
        val index = areaMetadata.withIndex().minByOrNull { (_,f)->getDistance(latitude, longitude, f.label_location) }?.index
        return areaMetadata[index!!].name
    }

    /**
     * Find out the nearest region based on user's current location
     *
     * @param latitude user latitude
     * @param longitude user longitude
     * @param regionMetadata list of region location data used by PM2.5
     * @return nearest region
     */
    private fun getPM25Region(
        latitude: Double,
        longitude: Double,
        regionMetadata: List<PMMetadataProperty>
    ): String {
        val index = regionMetadata.withIndex().minByOrNull { (_,f)->getDistance(latitude, longitude, f.label_location) }?.index
        return regionMetadata[index!!].name
    }

    /**
     * Find out the nearest temperature location based on user's current location
     *
     * @param latitude user latitude
     * @param longitude user longitude
     * @param stations list of temperature stations
     * @return nearest temperature station id
     */
    private fun getTempStationId(
        latitude: Double,
        longitude: Double,
        stations: List<TemperatureStationProperty>
    ): String {
//        val distanceResultList = stations.map { getDistance(latitude, longitude, it.location) }
//        val index = distanceResultList.withIndex().minByOrNull {(_,f)->f}?.index
        val index = stations.withIndex().minByOrNull{ (_,f)->getDistance(latitude, longitude, f.location) }?.index
        return stations[index!!].id
    }

    /**
     * Compute distance between two location in the map
     *
     * @param latitude user latitude
     * @param longitude user longitude
     * @param locationProperty Weather info location
     * @return distance between two points
     */
    private fun getDistance (
        latitude: Double,
        longitude: Double,
        locationProperty: LocationProperty
    ): Double {
        val dLat = deg2rad(latitude - locationProperty.latitude)
        val dLon = deg2rad(longitude - locationProperty.longitude)
        val a = Math.sin(dLat/2) * Math.cos(dLat/2) +
                Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(locationProperty.latitude)) *
                Math.sin(dLon/2) * Math.sin(dLon/2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
        return Radius * c
    }

    /**
     * Convert degree to radian
     *
     * @param deg Degree
     * @return Radian
     */
    private fun deg2rad(deg: Double): Double {
        return deg * (Math.PI/180)
    }


}