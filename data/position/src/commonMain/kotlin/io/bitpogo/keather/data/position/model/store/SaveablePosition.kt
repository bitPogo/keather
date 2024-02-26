/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.position.model.store

import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.Longitude

class SaveablePosition(
    val longitude: Longitude,
    val latitude: Latitude,
)
