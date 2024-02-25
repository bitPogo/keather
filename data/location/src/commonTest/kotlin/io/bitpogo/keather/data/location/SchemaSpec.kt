/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.location

import app.cash.sqldelight.async.coroutines.awaitAsOne
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFails
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.annotations.IgnoreJs
import tech.antibytes.util.test.annotations.RobolectricConfig
import tech.antibytes.util.test.annotations.RobolectricTestRunner
import tech.antibytes.util.test.annotations.RunWithRobolectricTestRunner
import tech.antibytes.util.test.coroutine.AsyncTestReturnValue
import tech.antibytes.util.test.coroutine.clearBlockingTest
import tech.antibytes.util.test.coroutine.resolveMultiBlockCalls
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.mustBe

@RobolectricConfig(manifest = "--none")
@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class SchemaSpec {
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
    @IgnoreJs
    @JsName("fn0")
    fun `Given a it will not add a new entry if the position for it is already set`(): AsyncTestReturnValue {
        runBlockingTest {
            // Given
            val longitude: Double = fixture.fixture()
            val latitude: Double = fixture.fixture()
            val name: String = fixture.fixture()
            val country: String = fixture.fixture()
            val region: String = fixture.fixture()

            // When
            assertFails {
                db.dataBase.locationQueries.set(
                    longitude = longitude,
                    latitude = latitude,
                    name = name,
                    region = region,
                    country = country,
                )
            }
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn1")
    fun `Given set is called it sets a new dataset`(): AsyncTestReturnValue {
        runBlockingTest {
            // Given
            val longitude: Double = fixture.fixture()
            val latitude: Double = fixture.fixture()
            val name: String = fixture.fixture()
            val country: String = fixture.fixture()
            val region: String = fixture.fixture()

            // When
            db.dataBase.positionQueries.set(long = longitude, lat = latitude)
            db.dataBase.locationQueries.set(
                longitude = longitude,
                latitude = latitude,
                name = name,
                region = region,
                country = country,
            )
            val hasEntry = db.dataBase.locationQueries.contains(
                long = longitude,
                lat = latitude,
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
    @JsName("fn3")
    fun `Given remove is called on a position the location is removed as well`(): AsyncTestReturnValue {
        runBlockingTest {
            // Given
            val longitude: Double = fixture.fixture()
            val latitude: Double = fixture.fixture()
            val name: String = fixture.fixture()
            val country: String = fixture.fixture()
            val region: String = fixture.fixture()

            // When
            db.dataBase.positionQueries.set(long = longitude, lat = latitude)
            db.dataBase.locationQueries.set(
                longitude = longitude,
                latitude = latitude,
                name = name,
                region = region,
                country = country,
            )
            db.dataBase.positionQueries.clear()
            val hasEntry = db.dataBase.locationQueries.contains(
                long = longitude,
                lat = latitude,
            ).awaitAsOne()

            // Then
            try {
                hasEntry mustBe false
            } catch (_: Throwable) {
                hasEntry mustBe 0
            }
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn2")
    fun `Given fetch is called it returns the latest preexisting dataset`(): AsyncTestReturnValue {
        runBlockingTest {
            // Given
            val longitude: Double = fixture.fixture()
            val latitude: Double = fixture.fixture()
            val name: String = fixture.fixture()
            val country: String = fixture.fixture()
            val region: String = fixture.fixture()

            // When
            db.dataBase.positionQueries.set(long = longitude, lat = latitude)
            db.dataBase.locationQueries.set(
                longitude = longitude,
                latitude = latitude,
                name = name,
                region = region,
                country = country,
            )
            val entry = db.dataBase.locationQueries.fetch().awaitAsOne()

            // Then
            entry mustBe Location(
                longitude = longitude,
                latitude = latitude,
                name = name,
                region = region,
                country = country,
            )
        }

        return resolveMultiBlockCalls()
    }
}
