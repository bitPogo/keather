/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.entity

import kotlin.jvm.JvmInline

@JvmInline
value class Longitude(val long: Double)

@JvmInline
value class Latitude(val lat: Double)

data class Position(
    val latitude: Latitude,
    val longitude: Longitude,
)

@JvmInline
value class Name(val name: Double)

@JvmInline
value class Region(val region: Double)

@JvmInline
value class Country(val country: Double)

data class Location(
    val name: Name,
    val region: Region,
    val country: Country,
    val position: Position,
)
