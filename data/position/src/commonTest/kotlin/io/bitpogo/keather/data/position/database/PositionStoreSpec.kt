/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.position.database

import app.cash.sqldelight.async.coroutines.awaitAsOne
import io.bitpogo.keather.data.position.PositionRepositoryContract
import io.bitpogo.keather.data.position.model.store.SaveablePosition
import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.Longitude
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.annotations.RobolectricConfig
import tech.antibytes.util.test.annotations.RobolectricTestRunner
import tech.antibytes.util.test.annotations.RunWithRobolectricTestRunner
import tech.antibytes.util.test.coroutine.AsyncTestReturnValue
import tech.antibytes.util.test.coroutine.clearBlockingTest
import tech.antibytes.util.test.coroutine.resolveMultiBlockCalls
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@RobolectricConfig(manifest = "--none")
@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class PositionStoreSpec {
    private val fixture = kotlinFixture()
    private val db = DatabaseDriver()

    @BeforeTest
    fun setup() {
        clearBlockingTest()
        runBlockingTest {
            db.open()
        }
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    @JsName("fn1")
    fun `Given save is called with a Position it saves it`(): AsyncTestReturnValue {
        // Given
        val position = SaveablePosition(
            Longitude(fixture.fixture()),
            Latitude(fixture.fixture()),
        )

        // When
        runBlockingTest {
            PositionStore(db.dataBase.positionQueries).savePosition(position)

            // Then
            val hasEntry = db.dataBase.positionQueries.contains(
                position.longitude.long,
                position.latitude.lat,
            ).awaitAsOne()

            // Then
            try {
                hasEntry mustBe true
            } catch (_: Throwable) {
                hasEntry mustBe 1 // Js
            }
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn2")
    fun `Given fetchPosition is called it propagates an IllegalStateError if no position is available`(): AsyncTestReturnValue {
        // When
        runBlockingTest {
            val actual = PositionStore(db.dataBase.positionQueries).fetchPosition()

            // Then
            actual.exceptionOrNull() fulfils IllegalStateException::class
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn3")
    fun `Given fetchPosition is called it propagates a previous saved  Position`(): AsyncTestReturnValue {
        // Given
        val position = SaveablePosition(
            Longitude(fixture.fixture()),
            Latitude(fixture.fixture()),
        )

        // When
        runBlockingTest {
            db.dataBase.positionQueries.set(position.longitude.long, position.latitude.lat)

            val entry = PositionStore(db.dataBase.positionQueries).fetchPosition()

            // Then
            entry.getOrNull() mustBe position
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils PositionStore`(): AsyncTestReturnValue {
        runBlockingTest {
            PositionStore(db.dataBase.positionQueries) fulfils PositionRepositoryContract.Store::class
        }

        return resolveMultiBlockCalls()
    }
}
