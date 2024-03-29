/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.position.database

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import tech.antibytes.keather.data.position.Position as DTO
import tech.antibytes.keather.data.position.PositionQueries // <-- this should be an interface
import tech.antibytes.keather.data.position.PositionRepositoryContract
import tech.antibytes.keather.data.position.model.store.SaveablePosition
import tech.antibytes.keather.entity.Latitude
import tech.antibytes.keather.entity.Longitude

internal class PositionStore(
    private val queries: PositionQueries,
) : PositionRepositoryContract.Store {
    override suspend fun savePosition(position: SaveablePosition) {
        return queries.set(
            longitude = position.longitude.long,
            latitude = position.latitude.lat,
        )
    }

    private fun DTO.map(): SaveablePosition {
        return SaveablePosition(
            longitude = Longitude(longitude),
            latitude = Latitude(latitude),
        )
    }

    override suspend fun fetchPosition(): Result<SaveablePosition> {
        val position = queries.fetch().awaitAsOneOrNull()

        return if (position == null) {
            Result.failure(IllegalStateException())
        } else {
            Result.success(position.map())
        }
    }
}
