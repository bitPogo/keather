/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.locator

import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.Location
import io.bitpogo.keather.entity.Longitude
import io.bitpogo.keather.interactor.repository.locator.LocatorContract
import io.bitpogo.keather.locator.AppleLocatorContractProtocol
import io.bitpogo.keather.locator.LocationResultContractProtocol
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalForeignApi::class)
internal class Locator(
    private val appleLocator: AppleLocatorContractProtocol,
) : LocatorContract.Locator {
    private fun LocationResultContractProtocol.resolveLocation(): Result<Location> {
        val error = this.error()
        return if (error == null) {
            val deviceLocation = this.success()!!
            Result.success(
                Location(
                    longitude = Longitude(deviceLocation.longitude()),
                    latitude = Latitude(deviceLocation.latitude()),
                ),
            )
        } else {
            Result.failure(Error(error.domain))
        }
    }

    private fun LocationResultContractProtocol?.mapWrapper(): Result<Location> {
        return this?.resolveLocation() ?: Result.failure(IllegalStateException())
    }

    override suspend fun getCurrentLocation(): Result<Location> = coroutineScope {
        val location = Channel<Result<Location>>()

        appleLocator.locateWithCallback { locationWrapper ->
            launch {
                location.send(locationWrapper.mapWrapper())
            }
        }

        location.receive()
    }
}
