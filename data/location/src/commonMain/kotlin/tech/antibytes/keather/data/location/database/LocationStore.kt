/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.location.database

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import tech.antibytes.keather.data.location.Location as DTO
import tech.antibytes.keather.data.location.LocationQueries
import tech.antibytes.keather.data.location.LocationRepositoryContract
import tech.antibytes.keather.data.location.model.store.SaveableLocation
import tech.antibytes.keather.entity.Country
import tech.antibytes.keather.entity.Latitude
import tech.antibytes.keather.entity.Longitude
import tech.antibytes.keather.entity.Name
import tech.antibytes.keather.entity.Region

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
