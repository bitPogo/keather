/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.entity

import kotlin.jvm.JvmInline

@JvmInline
value class Name(val name: String)

@JvmInline
value class Region(val region: String)

@JvmInline
value class Country(val country: String)

data class Location(
    val name: Name,
    val region: Region,
    val country: Country,
)
