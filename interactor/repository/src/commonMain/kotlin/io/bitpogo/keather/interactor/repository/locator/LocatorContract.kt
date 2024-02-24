package io.bitpogo.keather.interactor.repository.locator

import io.bitpogo.keather.entity.Location

interface LocatorContract {
    fun interface Locator {
        suspend fun getCurrentLocation(): Result<Location>
    }
}
