package io.bitpogo.keather.locator

import io.bitpogo.keather.entity.Location

interface LocatorContract {
    fun interface Locator {
        suspend fun getCurrentLocation(): Result<Location>
    }
}
