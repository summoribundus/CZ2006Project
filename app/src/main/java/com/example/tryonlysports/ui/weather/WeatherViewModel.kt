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

private const val TAG = "WeatherViewModel"

class WeatherViewModel(application: Application, location: Location) : AndroidViewModel(application) {
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

    private val _temperature = MutableLiveData<Double>()
    val temperature: LiveData<Double> get() = _temperature

    val location = location

    private val _pm25 = MutableLiveData<Double>()
    val pm25 : LiveData<Double> get() = _pm25

    private val _uvi = MutableLiveData<Double>()
    val uvi : LiveData<Double> get() = _uvi

    private val _forecast = MutableLiveData<String>()
    val forecast: LiveData<String> get() = _forecast

    private val _response = MutableLiveData<String>()
    val response : LiveData<String> get() = _response

    private val _sunProtectionSuggestion = MutableLiveData<String>()
    val sunProtectionSuggestion : LiveData<String> get() = _sunProtectionSuggestion

    private val _outdoorActivitySuggestion = MutableLiveData<String>()
    val outdoorActivitySuggestion : LiveData<String> get() = _outdoorActivitySuggestion

    private val _activityDurationSuggestion = MutableLiveData<String>()
    val activityDurationSuggestion : LiveData<String> get() = _activityDurationSuggestion

    private val _heatWarningSuggestion = MutableLiveData<String>()
    val heatWarningSuggestion : LiveData<String> get() = _heatWarningSuggestion

    private val _weatherIcon = MutableLiveData<Drawable>()
    val weatherIcon : LiveData<Drawable> get() = _weatherIcon

    private val Radius = 6371

    private lateinit var weatherCondition : WeatherCondition

    init {
        weatherProperty()
    }

    private fun weatherProperty() {
        viewModelScope.launch (Dispatchers.Main) {
            getForecastProperty()
            getTemperatureProperty()
            getPMProperty()
            getUVIProperty()
        }
    }

    private lateinit var temperatureProperty: TemperatureProperty
    private lateinit var pmProperty: PMProperty
    private lateinit var forecastProperty: ForecastProperty
    private lateinit var uviProperty: UVIProperty

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


    private fun getSunProtectionSuggestion() {
        if (_uvi.value!! in 0.0..0.9) {
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

    private fun getActivityDurationSuggestion() {
        if (_pm25.value!! in 0.0..55.0) {
            _activityDurationSuggestion.value = "Continue with normal activities."
        } else if (_pm25.value!! in 56.0..150.0) {
            _activityDurationSuggestion.value = "Reduce strenuous outdoor activity."
        } else if (_pm25.value!! in 151.0..250.0) {
            _activityDurationSuggestion.value = "Avoid strenuous outdoor activity."
        } else {
            _activityDurationSuggestion.value = "Minimize all outdoor activity."
        }
    }

    private fun getHeatWarningSuggestion() {
        if (_temperature.value!! in 27.0..32.0) {
            _heatWarningSuggestion.value = "Fatigue possible with prolonged " +
                    "exposure and/or physical activity."
        } else if (_temperature.value!! in 33.0..39.9) {
            _heatWarningSuggestion.value = "Heatstroke, heat cramps, or heat " +
                    "exhaustion possible with prolonged exposure and/or physical activity."
        } else {
            _heatWarningSuggestion.value = "Not specific suggestion"
        }
    }

    private fun getForecastLocationName(
        latitude: Double,
        longitude: Double,
        areaMetadata: List<ForecastMetadataProperty>
    ): String {
        val index = areaMetadata.withIndex().minByOrNull { (_,f)->getDistance(latitude, longitude, f.label_location) }?.index
        return areaMetadata[index!!].name
    }

    private fun getPM25Region(
        latitude: Double,
        longitude: Double,
        regionMetadata: List<PMMetadataProperty>
    ): String {
        val index = regionMetadata.withIndex().minByOrNull { (_,f)->getDistance(latitude, longitude, f.label_location) }?.index
        return regionMetadata[index!!].name
    }

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

    private fun deg2rad(deg: Double): Double {
        return deg * (Math.PI/180)
    }


}