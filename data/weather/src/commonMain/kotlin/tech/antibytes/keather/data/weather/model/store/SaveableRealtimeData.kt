/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.weather.model.store

internal data class SaveableRealtimeData(
    val timestamp: Long,
    val temperatureInCelsius: Double,
    val windSpeedInKilometerPerHour: Double,
    val precipitationInMillimeter: Double,
)
