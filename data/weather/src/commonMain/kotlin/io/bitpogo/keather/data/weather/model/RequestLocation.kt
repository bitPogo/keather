/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.model

import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.Longitude

data class RequestLocation(
    val longitude: Longitude,
    val latitude: Latitude
) {
    override fun toString(): String = "${latitude.lat},${longitude.long}"
}
