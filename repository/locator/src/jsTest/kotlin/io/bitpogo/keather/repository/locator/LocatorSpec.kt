/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.repository.locator

import io.bitpogo.keather.interactor.repository.locator.LocatorContract
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import web.geolocation.GeolocationPositionError
import web.navigator.navigator

class LocatorSpec {
    private val fixture = kotlinFixture()

    @Test
    @JsName("fn1")
    fun `Given getCurrentLocation is called it fails if no geolocation service is available`() = runTest {
        // When
        val result = Locator(null).getCurrentLocation()

        // Then
        result.exceptionOrNull() fulfils IllegalStateException::class
    }

    @Test
    @JsName("fn2")
    fun `Given getCurrentLocation is called it fails if no geolocation service is available(undefinded)`() = runTest {
        // When
        val result = Locator(undefined).getCurrentLocation()

        // Then
        result.exceptionOrNull() fulfils IllegalStateException::class
    }

    @Test
    @JsName("fn3")
    fun `Given getCurrentLocation is called it fails the locationservices fails`() = runTest {
        // Given
        val errorCode = GeolocationPositionError.PERMISSION_DENIED
        val error = js("{ code: errorCode }")
        val getCurrentPosition = { _: dynamic, onError: dynamic -> onError(error) }
        val fake = js("{ getCurrentPosition: getCurrentPosition}")

        // When
        val result = Locator(fake).getCurrentLocation()

        // Then
        result.exceptionOrNull() fulfils Error::class
        result.exceptionOrNull()?.message mustBe errorCode.toString()
    }

    @Test
    @JsName("fn4")
    fun `Given getCurrentLocation is called it propagates Locations`() = runTest {
        // Given
        val latitude: Double = fixture.fixture()
        val longitude: Double = fixture.fixture()
        val location = js("{ coords: { longitude: longitude, latitude: latitude } }")
        val getCurrentPosition = { onSuccess: dynamic, _: dynamic -> onSuccess(location) }
        val fake = js("{ getCurrentPosition: getCurrentPosition}")

        // When
        val result = Locator(fake).getCurrentLocation()

        // Then
        result.getOrNull()?.longitude?.long mustBe longitude
        result.getOrNull()?.latitude?.lat mustBe latitude
    }

    @Test
    @JsName("fn0")
    fun `It fulfils Locator`() {
        Locator(navigator.geolocation) fulfils LocatorContract.Locator::class
    }
}
