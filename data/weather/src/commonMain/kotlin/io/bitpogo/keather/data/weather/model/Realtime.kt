/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
internal data class Realtime(
    @SerialName("last_updated_epoch")
    val lastUpdateTimestamp: Long,
    @SerialName("last_updated")
    val lastUpdateDate: String,
    @SerialName("temp_c")
    val temperatureInCelsius: Double,
    @SerialName("temp_f")
    val temperatureInFahrenheit: Double,
    @SerialName("is_day")
    val isDay: Int,
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
    @SerialName("uv")
    val uvIndex: Double,
    val condition: Condition,
)
