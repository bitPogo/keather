/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.position

import io.bitpogo.keather.data.position.model.store.SaveablePosition
import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.Longitude
import io.bitpogo.keather.entity.Position
import io.bitpogo.keather.interactor.repository.RepositoryContract
import kotlin.js.JsName
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.verification.verify
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@OptIn(KMockExperimental::class)
@KMock(
    PositionRepositoryContract.Locator::class,
    PositionRepositoryContract.Store::class,
)
class PositionRepositorySpec {
    private val fixture = kotlinFixture()
    private val locator: LocatorMock = kmock()
    private val store: StoreMock = kmock(relaxUnitFun = true)

    @BeforeTest
    fun setUp() {
        locator._clearMock()
        store._clearMock()
    }

    @Test
    @JsName("fn1")
    fun `Given refresh is called it fetches a new location from a Device and propagates error`() = runTest {
        // Given
        val error = IllegalStateException()

        locator._fetchPosition returns Result.failure(error)

        // When
        val actual = PositionRepository(
            StandardTestDispatcher(testScheduler),
            locator,
            store,
        ).refreshPosition(this).await()

        // Then
        actual.exceptionOrNull() fulfils IllegalStateException::class
    }

    @Test
    @JsName("fn2")
    fun `Given refresh is called it fetches a new location from a Device and saves it and propagates its result`() = runTest {
        // Given
        val position = SaveablePosition(
            Longitude(fixture.fixture()),
            Latitude(fixture.fixture()),
        )

        locator._fetchPosition returns Result.success(position)

        // When
        val actual = PositionRepository(
            StandardTestDispatcher(testScheduler),
            locator,
            store,
        ).refreshPosition(this).await()

        // Then
        actual.getOrNull() mustBe Position(
            latitude = position.latitude,
            longitude = position.longitude,
        )
        verify {
            store._savePosition.hasBeenStrictlyCalledWith(position)
        }
    }

    @Test
    @JsName("fn3")
    fun `Given fetchPosition is called it propagates errors`() = runTest {
        // Given
        val error = IllegalStateException()

        // When
        store._fetchPosition returns Result.failure(error)

        // Then
        val actual = PositionRepository(
            StandardTestDispatcher(testScheduler),
            locator,
            store,
        ).fetchPosition(this).await()

        // Then
        actual.exceptionOrNull() fulfils IllegalStateException::class
    }

    @Test
    @JsName("fn4")
    fun `Given fetchPosition is called it propagates stored results`() = runTest {
        // Given
        val position = SaveablePosition(
            Longitude(fixture.fixture()),
            Latitude(fixture.fixture()),
        )

        // When
        store._fetchPosition returns Result.success(position)

        // Then
        val actual = PositionRepository(
            StandardTestDispatcher(testScheduler),
            locator,
            store,
        ).fetchPosition(this).await()

        // Then
        actual.getOrNull() mustBe Position(
            latitude = position.latitude,
            longitude = position.longitude,
        )
    }

    @Test
    @JsName("fn0")
    fun `It fulfils PositionRepository`() {
        PositionRepository(
            UnconfinedTestDispatcher(),
            locator,
            store,
        ) fulfils RepositoryContract.PositionRepository::class
    }
}
