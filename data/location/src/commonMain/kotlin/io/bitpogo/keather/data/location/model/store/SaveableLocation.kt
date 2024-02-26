/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.location.model.store

import io.bitpogo.keather.entity.Country
import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.Longitude
import io.bitpogo.keather.entity.Name
import io.bitpogo.keather.entity.Region

data class SaveableLocation(
    val latitude: Latitude,
    val longitude: Longitude,
    val name: Name,
    val region: Region,
    val country: Country,
)
