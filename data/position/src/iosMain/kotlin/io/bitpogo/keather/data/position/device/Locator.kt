/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.position.device

import io.bitpogo.keather.data.position.PositionRepositoryContract
import io.bitpogo.keather.data.position.locator.AppleLocatorContractProtocol
import io.bitpogo.keather.data.position.locator.LocationResultContractProtocol
import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.Longitude
import io.bitpogo.keather.entity.Position
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalForeignApi::class)
internal class Locator(
    private val appleLocator: AppleLocatorContractProtocol,
) : PositionRepositoryContract.Locator {
    private fun LocationResultContractProtocol.resolveLocation(): Result<Position> {
        val error = this.error()
        return if (error == null) {
            val deviceLocation = this.success()!!
            Result.success(
                Position(
                    longitude = Longitude(deviceLocation.longitude()),
                    latitude = Latitude(deviceLocation.latitude()),
                ),
            )
        } else {
            Result.failure(Error(error.domain))
        }
    }

    private fun LocationResultContractProtocol?.mapWrapper(): Result<Position> {
        return this?.resolveLocation() ?: Result.failure(IllegalStateException())
    }

    override suspend fun fetchPosition(): Result<Position> = coroutineScope {
        val position = Channel<Result<Position>>()

        appleLocator.locateWithCallback { locationWrapper ->
            launch {
                position.send(locationWrapper.mapWrapper())
            }
        }

        position.receive()
    }
}
