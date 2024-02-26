/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.entity

import kotlin.Error

sealed class WeatherError(message: String? = null) : Error(message) {
    data object PlaceholderError : WeatherError()
}
