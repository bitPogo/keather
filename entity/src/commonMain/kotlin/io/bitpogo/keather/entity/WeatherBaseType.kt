/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.entity

import kotlin.jvm.JvmInline

@JvmInline
value class Timestamp(val timestamp: Long)

@JvmInline
value class WindSpeedInKpH(val speed: Double)

@JvmInline
value class TemperatureInCelsius(val temperature: Double)

@JvmInline
value class PrecipitationInMillimeter(val precipitation: Double)

@JvmInline
value class Possibility(val possibility: Long)
