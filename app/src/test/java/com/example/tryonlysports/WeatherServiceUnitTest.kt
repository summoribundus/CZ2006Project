package com.example.tryonlysports

import org.junit.Test

import org.junit.Assert.*

class WeatherServiceUnitTest {
    @Test
    fun mapToWeatherConditionTests() {
        var weatherCondition = WeatherServiceTestUtils.mapToWeatherCondition("fair")
        assertEquals(WeatherServiceTestUtils.WeatherCondition.FAIR, weatherCondition)

        weatherCondition = WeatherServiceTestUtils.mapToWeatherCondition("cloudy")
        assertEquals(WeatherServiceTestUtils.WeatherCondition.CLOUDY, weatherCondition)

        weatherCondition = WeatherServiceTestUtils.mapToWeatherCondition("cloudy(night)")
        assertEquals(WeatherServiceTestUtils.WeatherCondition.CLOUDY_NIGHT, weatherCondition)

        weatherCondition = WeatherServiceTestUtils.mapToWeatherCondition("partly cloudy")
        assertEquals(WeatherServiceTestUtils.WeatherCondition.PARTLY_CLOUDY, weatherCondition)

        weatherCondition = WeatherServiceTestUtils.mapToWeatherCondition("windy")
        assertEquals(WeatherServiceTestUtils.WeatherCondition.WINDY, weatherCondition)

        weatherCondition = WeatherServiceTestUtils.mapToWeatherCondition("showers")
        assertEquals(WeatherServiceTestUtils.WeatherCondition.SHOWER, weatherCondition)

        weatherCondition = WeatherServiceTestUtils.mapToWeatherCondition("thundery showers")
        assertEquals(WeatherServiceTestUtils.WeatherCondition.THUNDERY_SHOWER, weatherCondition)

        weatherCondition = WeatherServiceTestUtils.mapToWeatherCondition("rain")
        assertEquals(WeatherServiceTestUtils.WeatherCondition.SHOWER, weatherCondition)

        weatherCondition = WeatherServiceTestUtils.mapToWeatherCondition("hazy")
        assertEquals(WeatherServiceTestUtils.WeatherCondition.HAZY, weatherCondition)

        weatherCondition = WeatherServiceTestUtils.mapToWeatherCondition("overcast")
        assertEquals(WeatherServiceTestUtils.WeatherCondition.OVERCAST, weatherCondition)

        weatherCondition = WeatherServiceTestUtils.mapToWeatherCondition("hello")
        assertEquals(WeatherServiceTestUtils.WeatherCondition.UNKNOWN, weatherCondition)
    }

    @Test
    fun getSunProtectionSuggestionTests() {
        var sunProtectionSuggestion = WeatherServiceTestUtils.getSunProtectionSuggestion(0.0)
        assertEquals("No precaution needed.", sunProtectionSuggestion)

        sunProtectionSuggestion = WeatherServiceTestUtils.getSunProtectionSuggestion(1.0)
        assertEquals("Wear sunglasses on bright days.", sunProtectionSuggestion)

        sunProtectionSuggestion = WeatherServiceTestUtils.getSunProtectionSuggestion(2.0)
        assertEquals("Wear sunglasses on bright days.", sunProtectionSuggestion)

        sunProtectionSuggestion = WeatherServiceTestUtils.getSunProtectionSuggestion(3.0)
        assertEquals("Covering up and using sunscreen. ", sunProtectionSuggestion)

        sunProtectionSuggestion = WeatherServiceTestUtils.getSunProtectionSuggestion(5.0)
        assertEquals("Covering up and using sunscreen. ", sunProtectionSuggestion)

        sunProtectionSuggestion = WeatherServiceTestUtils.getSunProtectionSuggestion(6.0)
        assertEquals("Reduce outdoor activities at noon.", sunProtectionSuggestion)

        sunProtectionSuggestion = WeatherServiceTestUtils.getSunProtectionSuggestion(7.0)
        assertEquals("Reduce outdoor activities at noon.", sunProtectionSuggestion)

        sunProtectionSuggestion = WeatherServiceTestUtils.getSunProtectionSuggestion(8.0)
        assertEquals("Extra precautions needed. ", sunProtectionSuggestion)

        sunProtectionSuggestion = WeatherServiceTestUtils.getSunProtectionSuggestion(10.0)
        assertEquals("Extra precautions needed. ", sunProtectionSuggestion)

        sunProtectionSuggestion = WeatherServiceTestUtils.getSunProtectionSuggestion(11.0)
        assertEquals("Take all precautions. ", sunProtectionSuggestion)

        sunProtectionSuggestion = WeatherServiceTestUtils.getSunProtectionSuggestion(-1.0)
        assertEquals("Invalid value.", sunProtectionSuggestion)
    }

    @Test
    fun getOutdoorActivitySuggestionTests() {
        var outdoorActivitySuggestion = WeatherServiceTestUtils.getOutdoorActivitySuggestion(WeatherServiceTestUtils.WeatherCondition.FAIR)
        assertEquals("Nice day for outdoor activities.", outdoorActivitySuggestion)

        outdoorActivitySuggestion = WeatherServiceTestUtils.getOutdoorActivitySuggestion(WeatherServiceTestUtils.WeatherCondition.CLOUDY)
        assertEquals("Suitable for outdoor activities.", outdoorActivitySuggestion)

        outdoorActivitySuggestion = WeatherServiceTestUtils.getOutdoorActivitySuggestion(WeatherServiceTestUtils.WeatherCondition.SHOWER)
        assertEquals("Recommend to stay indoors.", outdoorActivitySuggestion)

        outdoorActivitySuggestion = WeatherServiceTestUtils.getOutdoorActivitySuggestion(WeatherServiceTestUtils.WeatherCondition.UNKNOWN)
        assertEquals("Unable to give suggestions.", outdoorActivitySuggestion)
    }

    @Test
    fun getActivityDurationSuggestionTests() {
        var activityDurationSuggestion = WeatherServiceTestUtils.getActivityDurationSuggestion(0.0)
        assertEquals("Continue with normal activities.", activityDurationSuggestion)

        activityDurationSuggestion = WeatherServiceTestUtils.getActivityDurationSuggestion(55.0)
        assertEquals("Continue with normal activities.", activityDurationSuggestion)

        activityDurationSuggestion = WeatherServiceTestUtils.getActivityDurationSuggestion(56.0)
        assertEquals("Reduce strenuous outdoor activity.", activityDurationSuggestion)

        activityDurationSuggestion = WeatherServiceTestUtils.getActivityDurationSuggestion(150.0)
        assertEquals("Reduce strenuous outdoor activity.", activityDurationSuggestion)

        activityDurationSuggestion = WeatherServiceTestUtils.getActivityDurationSuggestion(151.0)
        assertEquals("Avoid strenuous outdoor activity.", activityDurationSuggestion)

        activityDurationSuggestion = WeatherServiceTestUtils.getActivityDurationSuggestion(250.0)
        assertEquals("Avoid strenuous outdoor activity.", activityDurationSuggestion)

        activityDurationSuggestion = WeatherServiceTestUtils.getActivityDurationSuggestion(251.0)
        assertEquals("Minimize all outdoor activity.", activityDurationSuggestion)

        activityDurationSuggestion = WeatherServiceTestUtils.getActivityDurationSuggestion(-1.0)
        assertEquals("Invalid value.", activityDurationSuggestion)
    }

    @Test
    fun getHeatWarningSuggestionTests() {
        var heatWarningSuggestion = WeatherServiceTestUtils.getHeatWarningSuggestion(26.0)
        assertEquals("Not specific suggestion", heatWarningSuggestion)

        heatWarningSuggestion = WeatherServiceTestUtils.getHeatWarningSuggestion(27.0)
        assertEquals("Fatigue possible with prolonged activity.", heatWarningSuggestion)

        heatWarningSuggestion = WeatherServiceTestUtils.getHeatWarningSuggestion(32.0)
        assertEquals("Fatigue possible with prolonged activity.", heatWarningSuggestion)

        heatWarningSuggestion = WeatherServiceTestUtils.getHeatWarningSuggestion(33.0)
        assertEquals("Heatstroke possible with prolonged activity.", heatWarningSuggestion)

        heatWarningSuggestion = WeatherServiceTestUtils.getHeatWarningSuggestion(40.0)
        assertEquals("Heatstroke possible with prolonged activity.", heatWarningSuggestion)

        heatWarningSuggestion = WeatherServiceTestUtils.getHeatWarningSuggestion(41.0)
        assertEquals("Not specific suggestion", heatWarningSuggestion)
    }

    @Test
    fun getWeatherIconTests() {
        var id = WeatherServiceTestUtils.getWeatherIcon(WeatherServiceTestUtils.WeatherCondition.FAIR)
        assertEquals(R.drawable.ic_sun, id)

        id = WeatherServiceTestUtils.getWeatherIcon(WeatherServiceTestUtils.WeatherCondition.CLOUDY)
        assertEquals(R.drawable.ic_cloudiness, id)

        id = WeatherServiceTestUtils.getWeatherIcon(WeatherServiceTestUtils.WeatherCondition.CLOUDY_NIGHT)
        assertEquals(R.drawable.ic_clouds_cloudiness_moon_night_sky, id)

        id = WeatherServiceTestUtils.getWeatherIcon(WeatherServiceTestUtils.WeatherCondition.PARTLY_CLOUDY)
        assertEquals(R.drawable.ic_cloud_sun, id)

        id = WeatherServiceTestUtils.getWeatherIcon(WeatherServiceTestUtils.WeatherCondition.WINDY)
        assertEquals(R.drawable.ic_weather_wind, id)

        id = WeatherServiceTestUtils.getWeatherIcon(WeatherServiceTestUtils.WeatherCondition.SHOWER)
        assertEquals(R.drawable.ic_loud_rain, id)

        id = WeatherServiceTestUtils.getWeatherIcon(WeatherServiceTestUtils.WeatherCondition.THUNDERY_SHOWER)
        assertEquals(R.drawable.ic_cloud_rain_thunderstorm_lightning_storm, id)

        id = WeatherServiceTestUtils.getWeatherIcon(WeatherServiceTestUtils.WeatherCondition.HAZY)
        assertEquals(R.drawable.ic_haze, id)

        id = WeatherServiceTestUtils.getWeatherIcon(WeatherServiceTestUtils.WeatherCondition.OVERCAST)
        assertEquals(R.drawable.ic_overcast, id)

        id = WeatherServiceTestUtils.getWeatherIcon(WeatherServiceTestUtils.WeatherCondition.UNKNOWN)
        assertEquals(R.drawable.ic_unknown, id)
    }
}