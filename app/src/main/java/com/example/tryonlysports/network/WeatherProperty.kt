package com.example.tryonlysports.network


// Common
data class LocationProperty(val latitude: Double,
                            val longitude: Double)

data class APIInfoProperty(val status: String)


// Temperature
data class TemperatureProperty(val metadata: TemperatureMetadataProperty,
                               val items: List<TemperatureItemProperty>,
                               val api_info: APIInfoProperty)

data class TemperatureMetadataProperty(val stations: List<TemperatureStationProperty>,
                                       val reading_type: String,
                                       val reading_unit: String)

data class TemperatureStationProperty(val id: String,
                                      val device_id: String,
                                      val name: String,
                                      val location: LocationProperty)

data class TemperatureItemProperty(val timestamp: String,
                                   val readings: List<TemperatureReadingProperty>)

data class TemperatureReadingProperty(val station_id: String,
                                      val value: Double)


// UVI
data class UVIProperty(val api_info: APIInfoProperty,
                       val items: List<UVIItemProperty>)

data class UVIItemProperty(val timestamp: String,
                           val update_timestamp: String,
                           val index: List<UVIIndexProperty>)

data class UVIIndexProperty(val value: Double,
                            val timestamp: String)


// PM25
data class PMProperty(val api_info: APIInfoProperty,
                      val region_metadata: List<PMMetadataProperty>,
                      val items: List<PMItemProperty>)

data class PMMetadataProperty(val name: String,
                              val label_location: LocationProperty)

data class PMItemProperty(val timestamp: String,
                          val update_timestamp: String,
                          val readings: PMReadingProperty)

data class PMReadingProperty(val pm25_one_hourly: PMHourProperty)

data class PMHourProperty(val west: Double,
                          val east: Double,
                          val central: Double,
                          val south: Double,
                          val north: Double)


// Forecast
data class ForecastProperty(val area_metadata: List<ForecastMetadataProperty>,
                            val items: List<ForecastItemProperty>,
                            val api_info: APIInfoProperty)

data class ForecastMetadataProperty(val name: String,
                                    val label_location: LocationProperty)

data class ForecastItemProperty(val update_timestamp: String,
                                val timestamp: String,
                                val valid_period: ForecastValidProperty,
                                val forecasts: List<ForecastForecastProperty>)

data class ForecastValidProperty(val start: String,
                                 val end: String)

data class ForecastForecastProperty(val area: String,
                                    val forecast: String)
