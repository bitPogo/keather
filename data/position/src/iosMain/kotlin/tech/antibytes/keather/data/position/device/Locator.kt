/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.position.device

import tech.antibytes.keather.data.position.PositionRepositoryContract
import tech.antibytes.keather.data.position.locator.AppleLocator
import tech.antibytes.keather.data.position.locator.AppleLocatorContractProtocol
import tech.antibytes.keather.data.position.locator.LocationResultContractProtocol
import tech.antibytes.keather.data.position.model.store.SaveablePosition
import tech.antibytes.keather.entity.Latitude
import tech.antibytes.keather.entity.Longitude
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalForeignApi::class)
internal class Locator(
    private val appleLocator: AppleLocatorContractProtocol = AppleLocator(),
) : PositionRepositoryContract.Locator {
    private fun LocationResultContractProtocol.resolveLocation(): Result<SaveablePosition> {
        val error = this.error()
        return if (error == null) {
            val deviceLocation = this.success()!!
            Result.success(
                SaveablePosition(
                    longitude = Longitude(deviceLocation.longitude()),
                    latitude = Latitude(deviceLocation.latitude()),
                ),
            )
        } else {
            Result.failure(Error(error.domain))
        }
    }

    private fun LocationResultContractProtocol?.mapWrapper(): Result<SaveablePosition> {
        return this?.resolveLocation() ?: Result.failure(IllegalStateException())
    }

    override suspend fun fetchPosition(): Result<SaveablePosition> = coroutineScope {
        val position = Channel<Result<SaveablePosition>>()

        appleLocator.locateWithCallback { locationWrapper ->
            launch {
                position.send(locationWrapper.mapWrapper())
            }
        }

        position.receive()
    }
}
