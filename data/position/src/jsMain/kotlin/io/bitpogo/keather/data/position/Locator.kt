/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.position

import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.Longitude
import io.bitpogo.keather.entity.Position
import io.bitpogo.keather.interactor.repository.locator.LocatorContract
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import web.geolocation.Geolocation

internal class Locator(
    private val geolocation: Geolocation?,
) : LocatorContract.Locator {
    private suspend fun resolveLocation(): Result<Position> = coroutineScope {
        val position = Channel<Result<Position>>()
        geolocation!!.getCurrentPosition(
            successCallback = { success ->
                launch {
                    position.send(
                        Result.success(
                            Position(Latitude(success.coords.latitude), Longitude(success.coords.longitude)),
                        ),
                    )
                }
            },
            errorCallback = { error ->
                launch {
                    position.send(Result.failure(Error(error.code.toString())))
                }
            },
        )
        return@coroutineScope position.receive()
    }

    override suspend fun getCurrentLocation(): Result<Position> {
        return if (geolocation == null || geolocation == undefined) {
            Result.failure(IllegalStateException())
        } else {
            resolveLocation()
        }
    }
}
