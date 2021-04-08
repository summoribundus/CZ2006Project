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
 * @property latitude Latitude
 * @property longitude Longitude
 */
data class LocationProperty(val latitude: Double,
                            val longitude: Double)

/**
 * API property containing the status of response
 *
 * @property status The status of api
 */
data class APIInfoProperty(val status: String)


// Temperature
/**
 * Containing information on temperature
 *
 * @property metadata Metadata on temperature
 * @property items List of temperature
 * @property api_info Information about API
 */
data class TemperatureProperty(val metadata: TemperatureMetadataProperty,
                               val items: List<TemperatureItemProperty>,
                               val api_info: APIInfoProperty)

/**
 * Metadata on temperature
 *
 * @property stations List of information on temperature stations
 * @property reading_type Type of data retrieval
 * @property reading_unit The unit for temperature
 */
data class TemperatureMetadataProperty(val stations: List<TemperatureStationProperty>,
                                       val reading_type: String,
                                       val reading_unit: String)

/**
 * Information on temperature stations
 *
 * @property id Station id
 * @property device_id Station device id
 * @property name Name of the station
 * @property location Location information of the station
 */
data class TemperatureStationProperty(val id: String,
                                      val device_id: String,
                                      val name: String,
                                      val location: LocationProperty)

/**
 * Temperature item
 *
 * @property timestamp Timestamp of the creation of temperature reading
 * @property readings Temperature reading
 */
data class TemperatureItemProperty(val timestamp: String,
                                   val readings: List<TemperatureReadingProperty>)

/**
 * Temperature reading
 *
 * @property station_id Id of temperature station id
 * @property value Temperature
 */
data class TemperatureReadingProperty(val station_id: String,
                                      val value: Double)


// UVI
/**
 * UV index property
 *
 * @property api_info Information on API
 * @property items List of uv index item
 */
data class UVIProperty(val api_info: APIInfoProperty,
                       val items: List<UVIItemProperty>)

/**
 * UV index item
 *
 * @property timestamp Timestamp of data generation
 * @property update_timestamp Time the data is updated
 * @property index List of uvi index
 */
data class UVIItemProperty(val timestamp: String,
                           val update_timestamp: String,
                           val index: List<UVIIndexProperty>)

/**
 * UV index
 *
 * @property value UV index
 * @property timestamp Timestamp of data generation
 */
data class UVIIndexProperty(val value: Double,
                            val timestamp: String)


// PM25
/**
 * PM2.5 Property
 *
 * @property api_info Information on API status
 * @property region_metadata PM2.5 metadata on different region
 * @property items List of PM2.5 item
 */
data class PMProperty(val api_info: APIInfoProperty,
                      val region_metadata: List<PMMetadataProperty>,
                      val items: List<PMItemProperty>)

/**
 * Meta region data on PM2.5 information
 *
 * @property name Name of region
 * @property label_location Location of region
 */
data class PMMetadataProperty(val name: String,
                              val label_location: LocationProperty)

/**
 * PM2.5 item property
 *
 * @property timestamp Data generation timestamp
 * @property update_timestamp When the data is updated
 * @property readings PM2.5 readinng
 */
data class PMItemProperty(val timestamp: String,
                          val update_timestamp: String,
                          val readings: PMReadingProperty)

/**
 * PM2.5 reading (hour based)
 *
 * @property pm25_one_hourly The hour this PM2.5 reading belongs
 */
data class PMReadingProperty(val pm25_one_hourly: PMHourProperty)

/**
 * PM2.5 value of each location
 *
 * @property west PM2.5 of the west
 * @property east PM2.5 of the east
 * @property central PM2.5 of the central
 * @property south PM2.5 of the south
 * @property north PM2.5 of the north
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
 * @property area_metadata List of meta data on area forecast belongs
 * @property items List of forecast
 * @property api_info Information on api
 */
data class ForecastProperty(val area_metadata: List<ForecastMetadataProperty>,
                            val items: List<ForecastItemProperty>,
                            val api_info: APIInfoProperty)

/**
 * Meta data on weather forecast
 *
 * @property name Name of area this forecast belongs
 * @property label_location Location information of this area
 */
data class ForecastMetadataProperty(val name: String,
                                    val label_location: LocationProperty)

/**
 * Forecast item property
 *
 * @property update_timestamp Timestamp of the update of this timestamp
 * @property timestamp Data generation time stamp
 * @property valid_period Period when the data is valid
 * @property forecasts List of weather forecast
 */
data class ForecastItemProperty(val update_timestamp: String,
                                val timestamp: String,
                                val valid_period: ForecastValidProperty,
                                val forecasts: List<ForecastForecastProperty>)

/**
 * Valid period of weather forecast
 *
 * @property start The start of valid period
 * @property end The end of valud period
 */
data class ForecastValidProperty(val start: String,
                                 val end: String)

/**
 * Weather forecast
 *
 * @property area The location this forecast belongs
 * @property forecast Weather forecast
 */
data class ForecastForecastProperty(val area: String,
                                    val forecast: String)
