/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.position

import tech.antibytes.keather.data.position.model.store.SaveablePosition
import tech.antibytes.keather.entity.Position
import tech.antibytes.keather.interactor.repository.RepositoryContract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

internal class PositionRepository(
    private val dispatcher: CoroutineDispatcher,
    private val locator: PositionRepositoryContract.Locator,
    private val store: PositionRepositoryContract.Store,
) : RepositoryContract.PositionRepository {
    private fun <T> defer(
        scope: CoroutineScope,
        context: CoroutineContext = EmptyCoroutineContext,
        action: suspend CoroutineScope.() -> T,
    ): Deferred<T> = scope.async(context = context, block = action)

    private fun SaveablePosition.map(): Position {
        return Position(
            longitude = longitude,
            latitude = latitude,
        )
    }

    override fun refreshPosition(
        scope: CoroutineScope,
    ): Deferred<Result<Position>> {
        return defer(scope, dispatcher) {
            locator.fetchPosition().map {
                store.savePosition(it)

                it.map()
            }
        }
    }

    override fun fetchPosition(scope: CoroutineScope): Deferred<Result<Position>> {
        return defer(scope, dispatcher) {
            store.fetchPosition().map { it.map() }
        }
    }
}
