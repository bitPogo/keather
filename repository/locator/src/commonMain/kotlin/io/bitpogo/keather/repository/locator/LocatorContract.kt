package io.bitpogo.keather.repository.locator

import io.bitpogo.keather.entity.Location

interface LocatorContract {
    fun interface Locator {
        suspend fun getCurrentLocation(): Result<Location>
    }
}
