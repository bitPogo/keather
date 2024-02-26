/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.location.database

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import io.bitpogo.keather.data.location.Location as DTO
import io.bitpogo.keather.data.location.LocationQueries
import io.bitpogo.keather.data.location.LocationRepositoryContract
import io.bitpogo.keather.data.location.model.store.SaveableLocation
import io.bitpogo.keather.entity.Country
import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.Longitude
import io.bitpogo.keather.entity.Name
import io.bitpogo.keather.entity.Region

internal class LocationStore(
    private val queries: LocationQueries,
) : LocationRepositoryContract.Store {
    private fun DTO.toSaveableLocation(): SaveableLocation {
        return SaveableLocation(
            longitude = Longitude(longitude),
            latitude = Latitude(latitude),
            name = Name(name),
            region = Region(region),
            country = Country(country),
        )
    }

    override suspend fun fetchLocation(): Result<SaveableLocation> {
        val location = queries.fetch().awaitAsOneOrNull()

        return if (location == null) {
            Result.failure(IllegalStateException())
        } else {
            Result.success(location.toSaveableLocation())
        }
    }
}
