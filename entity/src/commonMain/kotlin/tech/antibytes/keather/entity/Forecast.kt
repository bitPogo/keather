/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.entity

data class Forecast(
    val timestamp: Timestamp,
    val maximumTemperatureInCelsius: TemperatureInCelsius,
    val minimumTemperatureInCelsius: TemperatureInCelsius,
    val averageTemperatureInCelsius: TemperatureInCelsius,
    val maximumWindSpeedInKilometerPerHour: WindSpeedInKpH,
    val precipitationInMillimeter: PrecipitationInMillimeter,
    val rainPossibility: Possibility,
)
