/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.weather.database

import app.cash.sqldelight.async.coroutines.awaitAsOne
import tech.antibytes.keather.data.location.model.store.SaveableLocation
import tech.antibytes.keather.data.weather.model.store.SaveableForecast
import tech.antibytes.keather.data.weather.model.store.SaveableRealtimeData
import tech.antibytes.keather.entity.Country
import tech.antibytes.keather.entity.Latitude
import tech.antibytes.keather.entity.Longitude
import tech.antibytes.keather.entity.Name
import tech.antibytes.keather.entity.Region
import tech.antibytes.keather.entity.ReturnState
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.annotations.AndroidOnly
import tech.antibytes.util.test.annotations.IgnoreJs
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
class WeatherStoreSpec {
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
    @AndroidOnly
    @JsName("fn1")
    fun `Given setLocation it propagates Errors`(): AsyncTestReturnValue {
        // Given
        val longitude: Double = fixture.fixture()
        val latitude: Double = fixture.fixture()
        val name: String = fixture.fixture()
        val country: String = fixture.fixture()
        val region: String = fixture.fixture()
        val location = SaveableLocation(
            longitude = Longitude(longitude),
            latitude = Latitude(latitude),
            name = Name(name),
            region = Region(region),
            country = Country(country),
        )

        runBlockingTest {
            // When
            val actual = WeatherStore(
                db.dataBase.weatherQueries,
                db.dataBase.locationQueries,
            ).setLocation(location)

            actual mustBe ReturnState.Failure
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn2")
    fun `Given setLocation it sets a new Location`(): AsyncTestReturnValue {
        // Given
        val longitude: Double = fixture.fixture()
        val latitude: Double = fixture.fixture()
        val name: String = fixture.fixture()
        val country: String = fixture.fixture()
        val region: String = fixture.fixture()
        val location = SaveableLocation(
            longitude = Longitude(longitude),
            latitude = Latitude(latitude),
            name = Name(name),
            region = Region(region),
            country = Country(country),
        )

        runBlockingTest {
            db.dataBase.positionQueries.set(longitude, latitude)

            // When
            val actual = WeatherStore(
                db.dataBase.weatherQueries,
                db.dataBase.locationQueries,
            ).setLocation(location)
            val hasEntry = db.dataBase.positionQueries.contains(longitude, latitude).awaitAsOne()

            actual mustBe ReturnState.Success

            try {
                hasEntry mustBe true
            } catch (_: Throwable) {
                hasEntry mustBe 1
            }
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn3")
    fun `Given setForecast is called it stores Forecasts`(): AsyncTestReturnValue {
        // Given
        val forecasts = listOf(
            SaveableForecast(
                timestamp = fixture.fixture(),
                maximumTemperatureInCelsius = fixture.fixture(),
                minimumTemperatureInCelsius = fixture.fixture(),
                averageTemperatureInCelsius = fixture.fixture(),
                maximumWindSpeedInKilometerPerHour = fixture.fixture(),
                precipitationInMillimeter = fixture.fixture(),
                rainPossibility = fixture.fixture(),
            ),
            SaveableForecast(
                timestamp = fixture.fixture(),
                maximumTemperatureInCelsius = fixture.fixture(),
                minimumTemperatureInCelsius = fixture.fixture(),
                averageTemperatureInCelsius = fixture.fixture(),
                maximumWindSpeedInKilometerPerHour = fixture.fixture(),
                precipitationInMillimeter = fixture.fixture(),
                rainPossibility = fixture.fixture(),
            ),
        )

        // When
        runBlockingTest {
            WeatherStore(
                db.dataBase.weatherQueries,
                db.dataBase.locationQueries,
            ).setForecasts(forecasts)

            // Then
            val entries = db.dataBase.weatherQueries.countForecast().awaitAsOne()

            entries mustBe forecasts.size.toLong()
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @IgnoreJs
    @JsName("fn4")
    fun `Given fetchForecasts is called it returns Forecasts`(): AsyncTestReturnValue {
        // Given
        val forecasts = listOf(
            SaveableForecast(
                timestamp = fixture.fixture(),
                maximumTemperatureInCelsius = fixture.fixture(),
                minimumTemperatureInCelsius = fixture.fixture(),
                averageTemperatureInCelsius = fixture.fixture(),
                maximumWindSpeedInKilometerPerHour = fixture.fixture(),
                precipitationInMillimeter = fixture.fixture(),
                rainPossibility = fixture.fixture(),
            ),
            SaveableForecast(
                timestamp = fixture.fixture(),
                maximumTemperatureInCelsius = fixture.fixture(),
                minimumTemperatureInCelsius = fixture.fixture(),
                averageTemperatureInCelsius = fixture.fixture(),
                maximumWindSpeedInKilometerPerHour = fixture.fixture(),
                precipitationInMillimeter = fixture.fixture(),
                rainPossibility = fixture.fixture(),
            ),
        )

        // When
        runBlockingTest {
            forecasts.forEach { forecast ->
                db.dataBase.weatherQueries.addForecast(
                    timestamp = forecast.timestamp,
                    maximumTemperatureInCelsius = forecast.maximumTemperatureInCelsius,
                    minimumTemperatureInCelsius = forecast.minimumTemperatureInCelsius,
                    averageTemperatureInCelsius = forecast.averageTemperatureInCelsius,
                    maximumWindSpeedInKilometerPerHour = forecast.maximumWindSpeedInKilometerPerHour,
                    precipitationInMillimeter = forecast.precipitationInMillimeter,
                    rainPossibility = forecast.rainPossibility,
                )
            }

            val actual = WeatherStore(
                db.dataBase.weatherQueries,
                db.dataBase.locationQueries,
            ).fetchForecasts()

            // Then

            actual mustBe forecasts
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn5")
    fun `Given setHistoricData is called it stores Forecasts`(): AsyncTestReturnValue {
        // Given
        val forecasts = listOf(
            SaveableForecast(
                timestamp = fixture.fixture(),
                maximumTemperatureInCelsius = fixture.fixture(),
                minimumTemperatureInCelsius = fixture.fixture(),
                averageTemperatureInCelsius = fixture.fixture(),
                maximumWindSpeedInKilometerPerHour = fixture.fixture(),
                precipitationInMillimeter = fixture.fixture(),
                rainPossibility = fixture.fixture(),
            ),
            SaveableForecast(
                timestamp = fixture.fixture(),
                maximumTemperatureInCelsius = fixture.fixture(),
                minimumTemperatureInCelsius = fixture.fixture(),
                averageTemperatureInCelsius = fixture.fixture(),
                maximumWindSpeedInKilometerPerHour = fixture.fixture(),
                precipitationInMillimeter = fixture.fixture(),
                rainPossibility = fixture.fixture(),
            ),
        )

        // When
        runBlockingTest {
            WeatherStore(
                db.dataBase.weatherQueries,
                db.dataBase.locationQueries,
            ).setHistoricData(forecasts)

            // Then
            val entries = db.dataBase.weatherQueries.countHistoricData().awaitAsOne()

            entries mustBe forecasts.size.toLong()
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @IgnoreJs
    @JsName("fn6")
    fun `Given fetchHistory is called it returns Forecasts`(): AsyncTestReturnValue {
        // Given
        val forecasts = listOf(
            SaveableForecast(
                timestamp = fixture.fixture(),
                maximumTemperatureInCelsius = fixture.fixture(),
                minimumTemperatureInCelsius = fixture.fixture(),
                averageTemperatureInCelsius = fixture.fixture(),
                maximumWindSpeedInKilometerPerHour = fixture.fixture(),
                precipitationInMillimeter = fixture.fixture(),
                rainPossibility = fixture.fixture(),
            ),
            SaveableForecast(
                timestamp = fixture.fixture(),
                maximumTemperatureInCelsius = fixture.fixture(),
                minimumTemperatureInCelsius = fixture.fixture(),
                averageTemperatureInCelsius = fixture.fixture(),
                maximumWindSpeedInKilometerPerHour = fixture.fixture(),
                precipitationInMillimeter = fixture.fixture(),
                rainPossibility = fixture.fixture(),
            ),
        )

        // When
        runBlockingTest {
            forecasts.forEach { forecast ->
                db.dataBase.weatherQueries.addHistoricData(
                    timestamp = forecast.timestamp,
                    maximumTemperatureInCelsius = forecast.maximumTemperatureInCelsius,
                    minimumTemperatureInCelsius = forecast.minimumTemperatureInCelsius,
                    averageTemperatureInCelsius = forecast.averageTemperatureInCelsius,
                    maximumWindSpeedInKilometerPerHour = forecast.maximumWindSpeedInKilometerPerHour,
                    precipitationInMillimeter = forecast.precipitationInMillimeter,
                    rainPossibility = forecast.rainPossibility,
                )
            }

            val actual = WeatherStore(
                db.dataBase.weatherQueries,
                db.dataBase.locationQueries,
            ).fetchHistoricData()

            // Then

            actual mustBe forecasts
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn7")
    fun `Given fetchRealtimeData is called it returns an Error if no RealtimeData is there`(): AsyncTestReturnValue {
        // Given

        runBlockingTest {
            // When
            val actual = WeatherStore(
                db.dataBase.weatherQueries,
                db.dataBase.locationQueries,
            ).fetchRealtimeData()

            // Then

            actual.exceptionOrNull() fulfils IllegalStateException::class
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @IgnoreJs
    @JsName("fn8")
    fun `Given fetchRealtimeData is called it propagates stored data`(): AsyncTestReturnValue {
        // Given
        val data = Realtime(
            timestamp = fixture.fixture(),
            temperatureInCelsius = fixture.fixture(),
            precipitationInMillimeter = fixture.fixture(),
            windSpeedInKilometerPerHour = fixture.fixture(),
        )

        runBlockingTest {
            db.dataBase.weatherQueries.setRealtimeData(
                timestamp = data.timestamp,
                temperatureInCelsius = data.temperatureInCelsius,
                windSpeedInKilometerPerHour = data.windSpeedInKilometerPerHour,
                precipitationInMillimeter = data.precipitationInMillimeter,
            )

            val actual = WeatherStore(
                db.dataBase.weatherQueries,
                db.dataBase.locationQueries,
            ).fetchRealtimeData()

            // Then
            actual.getOrNull() mustBe SaveableRealtimeData(
                timestamp = data.timestamp,
                temperatureInCelsius = data.temperatureInCelsius,
                windSpeedInKilometerPerHour = data.windSpeedInKilometerPerHour,
                precipitationInMillimeter = data.precipitationInMillimeter,
            )
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn9")
    fun `Given setRealtimeData is called it stores data`(): AsyncTestReturnValue {
        // Given
        val data = SaveableRealtimeData(
            timestamp = fixture.fixture(),
            temperatureInCelsius = fixture.fixture(),
            precipitationInMillimeter = fixture.fixture(),
            windSpeedInKilometerPerHour = fixture.fixture(),
        )

        runBlockingTest {
            WeatherStore(
                db.dataBase.weatherQueries,
                db.dataBase.locationQueries,
            ).setRealtimeData(data)

            // Then
            val hasEntry = db.dataBase.weatherQueries.containsRealtimeData(data.timestamp).awaitAsOne()

            try {
                hasEntry mustBe true
            } catch (_: Throwable) {
                hasEntry mustBe 1
            }
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils WeatherStore`() {
        runBlockingTest {
            WeatherStore(
                db.dataBase.weatherQueries,
                db.dataBase.locationQueries,
            ) fulfils WeatherStore::class
        }
    }
}
