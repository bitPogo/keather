/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.location.model.store

import tech.antibytes.keather.entity.Country
import tech.antibytes.keather.entity.Latitude
import tech.antibytes.keather.entity.Longitude
import tech.antibytes.keather.entity.Name
import tech.antibytes.keather.entity.Region

data class SaveableLocation(
    val latitude: Latitude,
    val longitude: Longitude,
    val name: Name,
    val region: Region,
    val country: Country,
)
