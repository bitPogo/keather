/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.model.local

internal data class SaveableRealtime(
    val temperatureInCelsius: Double,
    val windSpeedInKilometerPerHour: Double,
    val precipitationInMillimeter: Double,
)
