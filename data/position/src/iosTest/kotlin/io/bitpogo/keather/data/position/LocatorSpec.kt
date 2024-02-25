/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.position

import io.bitpogo.keather.data.position.locator.AppleLocationContractProtocol
import io.bitpogo.keather.data.position.locator.LocationResultContractProtocol
import io.bitpogo.keather.interactor.repository.locator.LocatorContract
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.test.runTest
import platform.Foundation.NSError
import platform.darwin.NSObject
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@OptIn(ExperimentalForeignApi::class)
class LocatorSpec {
    private val fixture = kotlinFixture()
    private val appleLocator = AppleLocatorContractProtocolMock()

    @BeforeTest
    fun setup() {
        appleLocator._clearMock()
    }

    @Test
    fun `Given getCurrentLocation is called it propagates an error if no result was returned`() = runTest {
        // Given
        appleLocator._locateWithCallback run { callBack ->
            callBack(null)
        }

        // When
        val result = Locator(appleLocator).getCurrentLocation()

        // Then
        result.exceptionOrNull() fulfils IllegalStateException::class
    }

    @Test
    fun `Given getCurrentLocation is called it propagates errors provided by the AppleLocator`() = runTest {
        // Given
        val error = NSError.errorWithDomain(
            fixture.fixture<String>(),
            23,
            emptyMap<Any?, Any?>(),
        )
        appleLocator._locateWithCallback run { callBack ->
            callBack(LocationResultKotlin(null, error))
        }

        // When
        val result = Locator(appleLocator).getCurrentLocation()

        // Then
        result.exceptionOrNull() fulfils Error::class
        result.exceptionOrNull()?.message mustBe error.domain
    }

    @Test
    fun `Given getCurrentLocation is called it propagates Locations provided by the AppleLocator`() = runTest {
        // Given
        val location = AppleLocationKotlin(fixture.fixture(), fixture.fixture())
        appleLocator._locateWithCallback run { callBack ->
            callBack(LocationResultKotlin(location, null))
        }

        // When
        val result = Locator(appleLocator).getCurrentLocation()

        // Then
        result.getOrNull()?.latitude?.lat mustBe location.latitude()
        result.getOrNull()?.longitude?.long mustBe location.longitude()
    }

    @Test
    fun `It fulfils Locator`() {
        Locator(AppleLocatorContractProtocolMock()) fulfils LocatorContract.Locator::class
    }
}

@OptIn(ExperimentalForeignApi::class)
private class AppleLocationKotlin(
    private val _latitude: Double,
    private val _longitude: Double,
) : AppleLocationContractProtocol, NSObject() {
    override fun latitude(): Double = _latitude
    override fun longitude(): Double = _longitude
}

@OptIn(ExperimentalForeignApi::class)
private class LocationResultKotlin(
    private val _success: AppleLocationContractProtocol?,
    private val _error: NSError?,
) : LocationResultContractProtocol, NSObject() {
    override fun error(): NSError? = _error
    override fun success(): AppleLocationContractProtocol? = _success
}
