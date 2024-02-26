/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.database

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOne
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
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
    @JsName("fn0")
    fun `Given setRealtimeData it stores a RealtimeData`(): AsyncTestReturnValue {
        runBlockingTest {
            // Given
            val timestamp: Long = fixture.fixture()
            val temperatureInCelsius: Double = fixture.fixture()
            val windSpeedInKilometerPerHour: Double = fixture.fixture()
            val precipitationInMillimeter: Double = fixture.fixture()

            // When
            db.dataBase.weatherQueries.setRealtimeData(
                timestamp,
                temperatureInCelsius,
                precipitationInMillimeter,
                windSpeedInKilometerPerHour,
            )
            val hasEntry = db.dataBase.weatherQueries.containsRealtimeData(timestamp).awaitAsOne()

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
    fun `Given setRealtimeData it stores only the latest SavableRealtimeData`(): AsyncTestReturnValue {
        runBlockingTest {
            // Given
            val timestamp: Long = fixture.fixture()
            val temperatureInCelsius: Double = fixture.fixture()
            val windSpeedInKilometerPerHour: Double = fixture.fixture()
            val precipitationInMillimeter: Double = fixture.fixture()

            // When
            db.dataBase.weatherQueries.setRealtimeData(
                timestamp,
                temperatureInCelsius,
                precipitationInMillimeter,
                windSpeedInKilometerPerHour,
            )
            db.dataBase.weatherQueries.setRealtimeData(
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
            )
            val hasEntry = db.dataBase.weatherQueries.containsRealtimeData(timestamp).awaitAsOne()

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
    @IgnoreJs
    @JsName("fn2")
    fun `Given fetchRealtimeData it returns the latest stored RealtimeData`(): AsyncTestReturnValue {
        runBlockingTest {
            // Given
            val timestamp: Long = fixture.fixture()
            val temperatureInCelsius: Double = fixture.fixture()
            val windSpeedInKilometerPerHour: Double = fixture.fixture()
            val precipitationInMillimeter: Double = fixture.fixture()

            // When
            db.dataBase.weatherQueries.setRealtimeData(
                timestamp,
                temperatureInCelsius,
                precipitationInMillimeter,
                windSpeedInKilometerPerHour,
            )
            val entry = db.dataBase.weatherQueries.fetchRealtimeData().awaitAsOne()

            // Then
            entry mustBe Realtime(
                timestamp,
                temperatureInCelsius,
                precipitationInMillimeter,
                windSpeedInKilometerPerHour,
            )
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn3")
    fun `Given addForecast it stores a Forecast`(): AsyncTestReturnValue {
        runBlockingTest {
            // Given
            val timestamp: Long = fixture.fixture()
            val maximumTemperatureInCelsius: Double = fixture.fixture()
            val minimumTemperatureInCelsius: Double = fixture.fixture()
            val averageTemperatureInCelsius: Double = fixture.fixture()
            val maximumWindSpeedInKilometerPerHour: Double = fixture.fixture()
            val precipitationInMillimeter: Double = fixture.fixture()
            val rainPossibility: Long = fixture.fixture()

            // When
            db.dataBase.weatherQueries.addForecast(
                timestamp,
                maximumTemperatureInCelsius,
                minimumTemperatureInCelsius,
                averageTemperatureInCelsius,
                maximumWindSpeedInKilometerPerHour,
                precipitationInMillimeter,
                rainPossibility,
            )
            val hasEntry = db.dataBase.weatherQueries.containsForecast(timestamp).awaitAsOne()

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
    @JsName("fn4")
    fun `Given addForecast it stores more than one Forecast`(): AsyncTestReturnValue {
        runBlockingTest {
            // When
            db.dataBase.weatherQueries.addForecast(
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
            )
            db.dataBase.weatherQueries.addForecast(
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
            )
            val entries = db.dataBase.weatherQueries.countForecast().awaitAsOne()

            // Then
            entries mustBe 2
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @IgnoreJs
    @JsName("fn5")
    fun `Given fetchForecasts returns the stored forecasts`(): AsyncTestReturnValue {
        runBlockingTest {
            // Given
            val forecast1 = Forecast(
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
            )
            val forecast2 = Forecast(
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
                fixture.fixture(),
            )

            // When
            db.dataBase.weatherQueries.addForecast(
                forecast1.timestamp,
                forecast1.maximumTemperatureInCelsius,
                forecast1.minimumTemperatureInCelsius,
                forecast1.averageTemperatureInCelsius,
                forecast1.maximumWindSpeedInKilometerPerHour,
                forecast1.precipitationInMillimeter,
                forecast1.rainPossibility,
            )
            db.dataBase.weatherQueries.addForecast(
                forecast2.timestamp,
                forecast2.maximumTemperatureInCelsius,
                forecast2.minimumTemperatureInCelsius,
                forecast2.averageTemperatureInCelsius,
                forecast2.maximumWindSpeedInKilometerPerHour,
                forecast2.precipitationInMillimeter,
                forecast2.rainPossibility,
            )
            val entries = db.dataBase.weatherQueries.fetchForecasts().awaitAsList()

            // Then
            entries mustBe listOf(forecast1, forecast2)
        }

        return resolveMultiBlockCalls()
    }
}
