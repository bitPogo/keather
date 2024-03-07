/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.weather.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class HistoryHour(
    @SerialName("time")
    val time: String,
    @SerialName("time_epoch")
    val timestamp: Long,
    @SerialName("temp_c")
    val temperatureInCelsius: Double,
    @SerialName("temp_f")
    val temperatureInFahrenheit: Double,
    @SerialName("is_day")
    val isDay: Int,
    @SerialName("uv")
    val uvIndex: Double,
    val condition: Condition,
    @SerialName("wind_mph")
    val windSpeedInMilesPerHour: Double,
    @SerialName("wind_kph")
    val windSpeedInKilometerPerHour: Double,
    @SerialName("wind_degree")
    val windDirectionInDegree: Double,
    @SerialName("wind_dir")
    val windDirectionInCompass: String,
    @SerialName("pressure_mb")
    val pressureInMillibar: Double,
    @SerialName("pressure_in")
    val pressureInInch: Double,
    @SerialName("precip_mm")
    val precipitationInMillimeter: Double,
    @SerialName("precip_in")
    val precipitationInInch: Double,
    val humidity: Int,
    @SerialName("cloud")
    val cloudCoverage: Int,
    @SerialName("feelslike_c")
    val feltTemperatureInCelsius: Double,
    @SerialName("feelslike_f")
    val feltTemperatureInFahrenheit: Double,
    @SerialName("vis_km")
    val visibilityInKilometer: Double,
    @SerialName("vis_miles")
    val visibilityInMiles: Double,
    @SerialName("gust_kph")
    val windGustInKilometerPerHour: Double,
    @SerialName("gust_mph")
    val windGustInMilesPerHour: Double,
    @SerialName("snow_cm")
    val snowHeightInCentimeter: Double,
    @SerialName("will_it_rain")
    val willRain: Int,
    @SerialName("will_it_snow")
    val willSnow: Int,
    @SerialName("chance_of_rain")
    val rainPossibility: Int,
    @SerialName("chance_of_snow")
    val snowPossibility: Int,
    @SerialName("windchill_c")
    val windchillInCelsius: Double,
    @SerialName("windchill_f")
    val windchillInFahrenheit: Double,
    @SerialName("heatindex_c")
    val heatIndexInCelsius: Double,
    @SerialName("heatindex_f")
    val heatIndexInFahrenheit: Double,
    @SerialName("dewpoint_c")
    val dewPointInCelsius: Double,
    @SerialName("dewpoint_f")
    val dewPointInFahrenheit: Double,
)

@Serializable
internal data class HistoryAstronomy(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    @SerialName("moon_phase")
    val moonPhase: String,
    @SerialName("moon_illumination")
    val moonIllumination: Double,
)

@Serializable
internal data class HistoryDay(
    @SerialName("uv")
    val uvIndex: Double,
    val condition: Condition,
    @SerialName("maxtemp_c")
    val maximumTemperatureInCelsius: Double,
    @SerialName("maxtemp_f")
    val maximumTemperatureInFahrenheit: Double,
    @SerialName("mintemp_c")
    val minimumTemperatureInCelsius: Double,
    @SerialName("mintemp_f")
    val minimumTemperatureInFahrenheit: Double,
    @SerialName("avgtemp_c")
    val averageTemperatureInCelsius: Double,
    @SerialName("avgtemp_f")
    val averageTemperatureInFahrenheit: Double,
    @SerialName("maxwind_mph")
    val maximumWindSpeedInMilesPerHour: Double,
    @SerialName("maxwind_kph")
    val maximumWindSpeedInKilometerPerHour: Double,
    @SerialName("totalprecip_mm")
    val precipitationInMillimeter: Double,
    @SerialName("totalprecip_in")
    val precipitationInInches: Double,
    @SerialName("totalsnow_cm")
    val snowHeightInCentimeter: Double,
    @SerialName("avgvis_miles")
    val averageTemperatureInMiles: Double,
    @SerialName("avgvis_km")
    val averageTemperatureInKilometer: Double,
    @SerialName("avghumidity")
    val averageHumidity: Int,
    @SerialName("daily_will_it_rain")
    val willRain: Int,
    @SerialName("daily_will_it_snow")
    val willSnow: Int,
    @SerialName("daily_chance_of_rain")
    val rainPossibility: Int,
    @SerialName("daily_chance_of_snow")
    val snowPossibility: Int,
)

@Serializable
internal data class HistoryDayContainer(
    val date: String,
    @SerialName("date_epoch")
    val timestamp: Long,
    val day: HistoryDay,
    @SerialName("hour")
    val hours: List<HistoryHour>,
    @SerialName("astro")
    val astronomy: HistoryAstronomy,
)

@Serializable
internal data class Timeline(
    @SerialName("forecastday")
    val history: List<HistoryDayContainer>,
)

@Serializable
internal data class History(
    val location: Location,
    @SerialName("forecast")
    val history: Timeline,
)
