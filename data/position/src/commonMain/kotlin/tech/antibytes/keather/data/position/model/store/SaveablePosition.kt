/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.position.model.store

import tech.antibytes.keather.entity.Latitude
import tech.antibytes.keather.entity.Longitude

data class SaveablePosition(
    val longitude: Longitude,
    val latitude: Latitude,
)
