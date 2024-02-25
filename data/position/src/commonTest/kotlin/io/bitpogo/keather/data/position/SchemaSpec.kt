/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.position

import app.cash.sqldelight.async.coroutines.awaitAsOne
import io.bitpogo.keather.data.location.Position
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.annotations.RobolectricConfig
import tech.antibytes.util.test.annotations.RobolectricTestRunner
import tech.antibytes.util.test.annotations.RunWithRobolectricTestRunner
import tech.antibytes.util.test.coroutine.AsyncTestReturnValue
import tech.antibytes.util.test.coroutine.resolveMultiBlockCalls
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.mustBe
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@RobolectricConfig(manifest = "--none")
@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class SchemaSpec {
    private val fixture = kotlinFixture()
    private val db = DatabaseDriver()

    @BeforeTest
    fun setup() {
        runBlockingTest {
            db.open()
        }
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    @JsName("fn0")
    fun `Given set is called it sets a new dataset`(): AsyncTestReturnValue {
        runBlockingTest {
            // Given
            val longitude: Double = fixture.fixture()
            val latitude: Double = fixture.fixture()

            // When
            db.dataBase.positionQueries.set(longitude, latitude)
            val hasEntry = db.dataBase.positionQueries.contains(longitude, latitude).awaitAsOne()

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
    @JsName("fn1")
    fun `Given remove is called it removes a preexisting dataset`(): AsyncTestReturnValue {
        runBlockingTest {
            // Given
            val longitude: Double = fixture.fixture()
            val latitude: Double = fixture.fixture()

            // When
            db.dataBase.positionQueries.set(longitude, latitude)
            db.dataBase.positionQueries.clear()
            val hasEntry = db.dataBase.positionQueries.contains(longitude, latitude).awaitAsOne()

            // Then
            try {
                hasEntry mustBe false
            } catch (_: Throwable) {
                hasEntry mustBe 0 // Js
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

            // When
            db.dataBase.positionQueries.set(longitude, latitude)
            val entry = db.dataBase.positionQueries.fetch().awaitAsOne()

            // Then
            entry mustBe Position(longitude, latitude)
        }

        return resolveMultiBlockCalls()
    }
}
