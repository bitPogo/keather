/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.position.device

import tech.antibytes.keather.data.position.PositionRepositoryContract
import tech.antibytes.keather.data.position.model.store.SaveablePosition
import tech.antibytes.keather.entity.Latitude
import tech.antibytes.keather.entity.Longitude
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import web.geolocation.Geolocation
import web.navigator.navigator

internal class Locator(
    private val geolocation: Geolocation? = navigator.geolocation,
) : PositionRepositoryContract.Locator {
    private suspend fun resolveLocation(): Result<SaveablePosition> = coroutineScope {
        val position = Channel<Result<SaveablePosition>>()
        geolocation!!.getCurrentPosition(
            successCallback = { success ->
                launch {
                    position.send(
                        Result.success(
                            SaveablePosition(
                                latitude = Latitude(success.coords.latitude),
                                longitude = Longitude(success.coords.longitude),
                            ),
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

    override suspend fun fetchPosition(): Result<SaveablePosition> {
        return if (geolocation == null || geolocation == undefined) {
            Result.failure(IllegalStateException())
        } else {
            resolveLocation()
        }
    }
}
