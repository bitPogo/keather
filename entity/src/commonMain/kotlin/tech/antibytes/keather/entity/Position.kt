/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.entity

import kotlin.jvm.JvmInline

@JvmInline
value class Longitude(val long: Double)

@JvmInline
value class Latitude(val lat: Double)

data class Position(
    val latitude: Latitude,
    val longitude: Longitude,
)
