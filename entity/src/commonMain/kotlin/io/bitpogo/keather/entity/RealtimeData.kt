/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.entity

data class RealtimeData(
    val temperatureInCelsius: TemperatureInCelsius,
    val windSpeedInKilometerPerHour: WindSpeedInKpH,
    val precipitationInMillimeter: PrecipitationInMillimeter,
)
