package com.example.tryonlysports.network

/**
 * Containing data classes for json parsing
 *
 * @author Li Rui
 */

// Common
/**
 * Location property containing latitude and longitude
 *
 * @param latitude Latitude
 * @param longitude Longitude
 */
data class LocationProperty(val latitude: Double,
                            val longitude: Double)

/**
 * API property containing the status of response
 *
 * @param status The status of api
 */
data class APIInfoProperty(val status: String)


// Temperature
/**
 * Containing information on temperature
 *
 * @param metadata Metadata on temperature
 * @param items List of temperature
 * @param api_info Information about API
 */
data class TemperatureProperty(val metadata: TemperatureMetadataProperty,
                               val items: List<TemperatureItemProperty>,
                               val api_info: APIInfoProperty)

/**
 * Metadata on temperature
 *
 * @param stations List of information on temperature stations
 * @param reading_type Type of data retrieval
 * @param reading_unit The unit for temperature
 */
data class TemperatureMetadataProperty(val stations: List<TemperatureStationProperty>,
                                       val reading_type: String,
                                       val reading_unit: String)

/**
 * Information on temperature stations
 *
 * @param id Station id
 * @param device_id Station device id
 * @param name Name of the station
 * @param location Location information of the station
 */
data class TemperatureStationProperty(val id: String,
                                      val device_id: String,
                                      val name: String,
                                      val location: LocationProperty)

/**
 * Temperature item
 *
 * @param timestamp Timestamp of the creation of temperature reading
 * @param readings Temperature reading
 */
data class TemperatureItemProperty(val timestamp: String,
                                   val readings: List<TemperatureReadingProperty>)

/**
 * Temperature reading
 *
 * @param station_id Id of temperature station id
 * @param value Temperature
 */
data class TemperatureReadingProperty(val station_id: String,
                                      val value: Double)


// UVI
/**
 * UV index property
 *
 * @param api_info Information on API
 * @param items List of uv index item
 */
data class UVIProperty(val api_info: APIInfoProperty,
                       val items: List<UVIItemProperty>)

/**
 * UV index item
 *
 * @param timestamp Timestamp of data generation
 * @param update_timestamp Time the data is updated
 * @param index List of uvi index
 */
data class UVIItemProperty(val timestamp: String,
                           val update_timestamp: String,
                           val index: List<UVIIndexProperty>)

/**
 * UV index
 *
 * @param value UV index
 * @param timestamp Timestamp of data generation
 */
data class UVIIndexProperty(val value: Double,
                            val timestamp: String)


// PM25
/**
 * PM2.5 Property
 *
 * @param api_info Information on API status
 * @param region_metadata PM2.5 metadata on different region
 * @param items List of PM2.5 item
 */
data class PMProperty(val api_info: APIInfoProperty,
                      val region_metadata: List<PMMetadataProperty>,
                      val items: List<PMItemProperty>)

/**
 * Meta region data on PM2.5 information
 *
 * @param name Name of region
 * @param label_location Location of region
 */
data class PMMetadataProperty(val name: String,
                              val label_location: LocationProperty)

/**
 * PM2.5 item property
 *
 * @param timestamp Data generation timestamp
 * @param update_timestamp When the data is updated
 * @param readings PM2.5 readinng
 */
data class PMItemProperty(val timestamp: String,
                          val update_timestamp: String,
                          val readings: PMReadingProperty)

/**
 * PM2.5 reading (hour based)
 *
 * @param pm25_one_hourly The hour this PM2.5 reading belongs
 */
data class PMReadingProperty(val pm25_one_hourly: PMHourProperty)

/**
 * PM2.5 value of each location
 *
 * @param west PM2.5 of the west
 * @param east PM2.5 of the east
 * @param central PM2.5 of the central
 * @param south PM2.5 of the south
 * @param north PM2.5 of the north
 */
data class PMHourProperty(val west: Double,
                          val east: Double,
                          val central: Double,
                          val south: Double,
                          val north: Double)


// Forecast
/**
 * Forecast property
 *
 * @param area_metadata List of meta data on area forecast belongs
 * @param items List of forecast
 * @param api_info Information on api
 */
data class ForecastProperty(val area_metadata: List<ForecastMetadataProperty>,
                            val items: List<ForecastItemProperty>,
                            val api_info: APIInfoProperty)

/**
 * Meta data on weather forecast
 *
 * @param name Name of area this forecast belongs
 * @param label_location Location information of this area
 */
data class ForecastMetadataProperty(val name: String,
                                    val label_location: LocationProperty)

/**
 * Forecast item property
 *
 * @param update_timestamp Timestamp of the update of this timestamp
 * @param timestamp Data generation time stamp
 * @param valid_period Period when the data is valid
 * @param forecasts List of weather forecast
 */
data class ForecastItemProperty(val update_timestamp: String,
                                val timestamp: String,
                                val valid_period: ForecastValidProperty,
                                val forecasts: List<ForecastForecastProperty>)

/**
 * Valid period of weather forecast
 *
 * @param start The start of valid period
 * @param end The end of valud period
 */
data class ForecastValidProperty(val start: String,
                                 val end: String)

/**
 * Weather forecast
 *
 * @param area The location this forecast belongs
 * @param forecast Weather forecast
 */
data class ForecastForecastProperty(val area: String,
                                    val forecast: String)
