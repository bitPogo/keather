/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.location

import io.bitpogo.keather.data.location.model.store.SaveableLocation
import io.bitpogo.keather.entity.Location
import io.bitpogo.keather.interactor.repository.RepositoryContract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

internal class LocationRepository(
    private val dispatcher: CoroutineDispatcher,
    private val store: LocationRepositoryContract.Store,
) : RepositoryContract.LocationRepository {
    private fun <T> defer(
        scope: CoroutineScope,
        context: CoroutineContext = EmptyCoroutineContext,
        action: suspend CoroutineScope.() -> T,
    ): Deferred<T> = scope.async(context = context, block = action)

    private fun map(location: SaveableLocation): Location {
        return Location(
            name = location.name,
            region = location.region,
            country = location.country,
        )
    }

    override suspend fun fetchLocation(
        scope: CoroutineScope,
    ): Deferred<Result<Location>> = defer(scope, dispatcher) {
        store.fetchLocation().map(::map)
    }
}
