package com.example.tryonlysports


internal object WeatherServiceTestUtils {
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

    fun mapToWeatherCondition(_forecast: String): WeatherCondition {
        var weatherCondition: WeatherCondition
        if (_forecast.contains("fair", true))
            weatherCondition = WeatherCondition.FAIR
        else if (_forecast.contains("cloudy", true)) {
            if (_forecast.contains("night", true))
                weatherCondition = WeatherCondition.CLOUDY_NIGHT
            else if (_forecast.contains("partly", true))
                weatherCondition = WeatherCondition.PARTLY_CLOUDY
            else
                weatherCondition = WeatherCondition.CLOUDY
        }
        else if (_forecast.contains("windy", true))
            weatherCondition = WeatherCondition.WINDY
        else if (_forecast.contains("showers", true)) {
            if (_forecast.contains("thundery", true))
                weatherCondition = WeatherCondition.THUNDERY_SHOWER
            else weatherCondition = WeatherCondition.SHOWER
        }
        else if (_forecast.contains("rain", true)) {
            weatherCondition = WeatherCondition.SHOWER
        }
        else if (_forecast.contains("hazy", true))
            weatherCondition = WeatherCondition.HAZY
        else if (_forecast.contains("overcast", true))
            weatherCondition = WeatherCondition.OVERCAST
        else weatherCondition = WeatherCondition.UNKNOWN
        return weatherCondition
    }

    fun getSunProtectionSuggestion(_uvi: Double): String {
        var _sunProtectionSuggestion = ""
        if (_uvi < 0.0) {
            _sunProtectionSuggestion = "Invalid value."
        } else if (_uvi in 0.0..0.9) {
            _sunProtectionSuggestion = "No precaution needed."
        } else if (_uvi in 1.0..2.0) {
            _sunProtectionSuggestion = "Wear sunglasses on bright days."
        } else if (_uvi in 3.0..5.0) {
            _sunProtectionSuggestion = "Covering up and using sunscreen. "
        } else if (_uvi in 6.0..7.0) {
            _sunProtectionSuggestion = "Reduce outdoor activities at noon."
        } else if (_uvi in 8.0..10.0) {
            _sunProtectionSuggestion = "Extra precautions needed. "
        } else {
            _sunProtectionSuggestion = "Take all precautions. "
        }
        return _sunProtectionSuggestion
    }

    fun getOutdoorActivitySuggestion(weatherCondition: WeatherCondition):String {
        var _outdoorActivitySuggestion = ""
        when (weatherCondition) {
            WeatherCondition.FAIR -> _outdoorActivitySuggestion = "Nice day for outdoor activities."
            WeatherCondition.CLOUDY_NIGHT, WeatherCondition.PARTLY_CLOUDY, WeatherCondition.CLOUDY, WeatherCondition.WINDY ->
                _outdoorActivitySuggestion = "Suitable for outdoor activities."
            WeatherCondition.SHOWER, WeatherCondition.THUNDERY_SHOWER, WeatherCondition.HAZY, WeatherCondition.OVERCAST ->
                _outdoorActivitySuggestion = "Recommend to stay indoors."
            WeatherCondition.UNKNOWN -> _outdoorActivitySuggestion = "Unable to give suggestions."
        }
        return  _outdoorActivitySuggestion
    }

    fun getActivityDurationSuggestion(_pm25: Double):String {
        var _activityDurationSuggestion = ""
        if (_pm25 < 0.0) {
            _activityDurationSuggestion = "Invalid value."
        } else if (_pm25 in 0.0..55.0) {
            _activityDurationSuggestion = "Continue with normal activities."
        } else if (_pm25 in 56.0..150.0) {
            _activityDurationSuggestion = "Reduce strenuous outdoor activity."
        } else if (_pm25 in 151.0..250.0) {
            _activityDurationSuggestion = "Avoid strenuous outdoor activity."
        } else {
            _activityDurationSuggestion = "Minimize all outdoor activity."
        }
        return _activityDurationSuggestion
    }

    fun getHeatWarningSuggestion(_temperature: Double): String {
        var _heatWarningSuggestion = ""
        if (_temperature in 27.0..32.0) {
            _heatWarningSuggestion = "Fatigue possible with prolonged activity."
        } else if (_temperature in 33.0..40.0) {
            _heatWarningSuggestion = "Heatstroke possible with prolonged activity."
        } else {
            _heatWarningSuggestion = "Not specific suggestion"
        }
        return  _heatWarningSuggestion
    }

    fun getWeatherIcon(weatherCondition: WeatherCondition): Int {
        var _weatherIcon = 0
        when (weatherCondition) {
            WeatherCondition.THUNDERY_SHOWER->
                _weatherIcon =
                        R.drawable.ic_cloud_rain_thunderstorm_lightning_storm
            WeatherCondition.SHOWER->
                _weatherIcon =  R.drawable.ic_loud_rain
            WeatherCondition.WINDY->
                _weatherIcon =  R.drawable.ic_weather_wind
            WeatherCondition.PARTLY_CLOUDY->
                _weatherIcon =  R.drawable.ic_cloud_sun
            WeatherCondition.CLOUDY->
                _weatherIcon =  R.drawable.ic_cloudiness
            WeatherCondition.FAIR->
                _weatherIcon =  R.drawable.ic_sun
            WeatherCondition.HAZY->
                _weatherIcon =  R.drawable.ic_haze
            WeatherCondition.OVERCAST->
                _weatherIcon =  R.drawable.ic_overcast
            WeatherCondition.CLOUDY_NIGHT->
                _weatherIcon =  R.drawable.ic_clouds_cloudiness_moon_night_sky
            WeatherCondition.UNKNOWN->
                _weatherIcon =  R.drawable.ic_unknown
        }
        return _weatherIcon
    }
}