/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.repository.locator

import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.Location
import io.bitpogo.keather.entity.Longitude
import io.bitpogo.keather.interactor.repository.locator.LocatorContract
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import web.geolocation.Geolocation

internal class Locator(
    private val geolocation: Geolocation?,
) : LocatorContract.Locator {
    private suspend fun resolveLocation(): Result<Location> = coroutineScope {
        val location = Channel<Result<Location>>()
        geolocation!!.getCurrentPosition(
            successCallback = { success ->
                launch {
                    location.send(
                        Result.success(
                            Location(Latitude(success.coords.latitude), Longitude(success.coords.longitude)),
                        ),
                    )
                }
            },
            errorCallback = { error ->
                launch {
                    location.send(Result.failure(Error(error.code.toString())))
                }
            },
        )
        return@coroutineScope location.receive()
    }

    override suspend fun getCurrentLocation(): Result<Location> {
        return if (geolocation == null || geolocation == undefined) {
            Result.failure(IllegalStateException())
        } else {
            resolveLocation()
        }
    }
}
