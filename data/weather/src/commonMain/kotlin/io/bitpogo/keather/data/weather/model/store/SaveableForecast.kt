/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.model.store

internal data class SaveableForecast(
    val timestamp: Long,
    val maximumTemperatureInCelsius: Double,
    val minimumTemperatureInCelsius: Double,
    val averageTemperatureInCelsius: Double,
    val maximumWindSpeedInKilometerPerHour: Double,
    val precipitationInMillimeter: Double,
    val rainPossibility: Long,
)
