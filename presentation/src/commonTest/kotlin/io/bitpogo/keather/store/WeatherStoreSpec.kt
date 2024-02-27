/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.store

import app.cash.turbine.test
import io.bitpogo.keather.entity.Country
import io.bitpogo.keather.entity.Forecast
import io.bitpogo.keather.entity.HistoricData
import io.bitpogo.keather.entity.LocalizedWeatherData
import io.bitpogo.keather.entity.Location
import io.bitpogo.keather.entity.Name
import io.bitpogo.keather.entity.Possibility
import io.bitpogo.keather.entity.PrecipitationInMillimeter
import io.bitpogo.keather.entity.RealtimeData
import io.bitpogo.keather.entity.Region
import io.bitpogo.keather.entity.TemperatureInCelsius
import io.bitpogo.keather.entity.Timestamp
import io.bitpogo.keather.entity.WindSpeedInKpH
import io.bitpogo.keather.presentation.ui.store.StoreContract
import io.bitpogo.keather.presentation.ui.store.command.CommandMock
import io.bitpogo.keather.presentation.ui.store.command.RefreshCommandsContract
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.verification.verify
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@OptIn(KMockExperimental::class)
@KMock(
    RefreshCommandsContract.Command::class,
)
class WeatherStoreSpec {
    private val fixture = kotlinFixture()
    private val interactor = WeatherInteractorFake()
    private val weatherData = LocalizedWeatherData(
        realtimeData = RealtimeData(
            temperatureInCelsius = TemperatureInCelsius(fixture.fixture()),
            windSpeedInKilometerPerHour = WindSpeedInKpH(fixture.fixture()),
            precipitationInMillimeter = PrecipitationInMillimeter(fixture.fixture()),
        ),
        forecast = listOf(
            Forecast(
                timestamp = Timestamp(fixture.fixture()),
                maximumTemperatureInCelsius = TemperatureInCelsius(fixture.fixture()),
                minimumTemperatureInCelsius = TemperatureInCelsius(fixture.fixture()),
                averageTemperatureInCelsius = TemperatureInCelsius(fixture.fixture()),
                maximumWindSpeedInKilometerPerHour = WindSpeedInKpH(fixture.fixture()),
                precipitationInMillimeter = PrecipitationInMillimeter(fixture.fixture()),
                rainPossibility = Possibility(fixture.fixture()),
            ),
        ),
        history = listOf(
            HistoricData(
                timestamp = Timestamp(fixture.fixture()),
                averageTemperatureInCelsius = TemperatureInCelsius(fixture.fixture()),
                maximumWindSpeedInKilometerPerHour = WindSpeedInKpH(fixture.fixture()),
                precipitationInMillimeter = PrecipitationInMillimeter(fixture.fixture()),
            ),
        ),
        location = Location(
            name = Name(fixture.fixture()),
            country = Country(fixture.fixture()),
            region = Region(fixture.fixture()),
        ),
    )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        interactor.reset(Result.failure(IllegalStateException()))
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @JsName("fn1")
    fun `It is in the initial state`() {
        WeatherStore(interactor).weatherData.value mustBe StoreContract.Initial
    }

    @Test
    @JsName("fn2")
    fun `Given refresh is called it goes into Loading and propagates Errors`() = runTest {
        // Given
        val error = RuntimeException()
        interactor.reset(Result.failure(error))

        // When
        val store = WeatherStore(interactor)

        store.weatherData.test {
            val weather = this
            store.error.test {
                val errors = this

                store.refresh()

                advanceUntilIdle()
                // Then
                weather.skipItems(1) // Initial
                weather.awaitItem() mustBe StoreContract.StartUpLoading
                weather.awaitItem() mustBe StoreContract.StartUpError

                errors.awaitItem() mustBe StoreContract.WeatherUIError

                interactor.invoked mustBe WeatherInteractorFake.InvocationType.REFRESH
            }
        }
    }

    @Test
    @JsName("fn3")
    fun `Given refresh is called it goes into Loading and propagates Values`() = runTest {
        // Given
        interactor.reset(Result.success(weatherData))

        // When
        val store = WeatherStore(interactor)

        store.weatherData.test {
            store.refresh()

            advanceUntilIdle()
            // Then
            skipItems(1) // Initial
            awaitItem() mustBe StoreContract.StartUpLoading
            awaitItem() mustBe StoreContract.Loaded(weatherData)

            interactor.invoked mustBe WeatherInteractorFake.InvocationType.REFRESH
        }
    }

    @Test
    @JsName("fn4")
    fun `Given refresh is called it goes into Loading and propagates Errors with errors if a previous value is available`() = runTest {
        // Given
        val error = RuntimeException()
        interactor.reset(
            listOf(Result.success(weatherData), Result.failure(error)),
        )

        // When
        val store = WeatherStore(interactor)

        store.weatherData.test {
            val weather = this
            store.error.test {
                val errors = this

                store.refresh()
                advanceUntilIdle()
                weather.skipItems(3)

                store.refresh()
                advanceUntilIdle()

                // Then
                weather.awaitItem() mustBe StoreContract.Loading(weatherData)
                weather.awaitItem() mustBe StoreContract.Error(weatherData)

                errors.awaitItem() mustBe StoreContract.WeatherUIError

                interactor.invoked mustBe WeatherInteractorFake.InvocationType.REFRESH
            }
        }
    }

    @Test
    @JsName("fn5")
    fun `Given refreshAll is called it goes into Loading and propagates Errors`() = runTest {
        // Given
        val error = RuntimeException()
        interactor.reset(Result.failure(error))

        // When
        val store = WeatherStore(interactor)

        store.weatherData.test {
            val weather = this
            store.error.test {
                val errors = this

                store.refreshAll()

                advanceUntilIdle()
                // Then
                weather.skipItems(1) // Initial
                weather.awaitItem() mustBe StoreContract.StartUpLoading
                weather.awaitItem() mustBe StoreContract.StartUpError

                errors.awaitItem() mustBe StoreContract.WeatherUIError

                interactor.invoked mustBe WeatherInteractorFake.InvocationType.REFRESH_ALL
            }
        }
    }

    @Test
    @JsName("fn6")
    fun `Given refreshAll is called it goes into Loading and propagates Values`() = runTest {
        // Given
        interactor.reset(Result.success(weatherData))

        // When
        val store = WeatherStore(interactor)

        store.weatherData.test {
            store.refreshAll()

            advanceUntilIdle()
            // Then
            skipItems(1) // Initial
            awaitItem() mustBe StoreContract.StartUpLoading
            awaitItem() mustBe StoreContract.Loaded(weatherData)

            interactor.invoked mustBe WeatherInteractorFake.InvocationType.REFRESH_ALL
        }
    }

    @Test
    @JsName("fn7")
    fun `Given refreshAll is called it goes into Loading and propagates Errors with errors if a previous value is available`() = runTest {
        // Given
        val error = RuntimeException()
        interactor.reset(
            listOf(Result.success(weatherData), Result.failure(error)),
        )

        // When
        val store = WeatherStore(interactor)

        store.weatherData.test {
            val weather = this
            store.error.test {
                val errors = this

                store.refreshAll()
                advanceUntilIdle()
                weather.skipItems(3)

                store.refreshAll()
                advanceUntilIdle()

                // Then
                weather.awaitItem() mustBe StoreContract.Loading(weatherData)
                weather.awaitItem() mustBe StoreContract.Error(weatherData)

                errors.awaitItem() mustBe StoreContract.WeatherUIError

                interactor.invoked mustBe WeatherInteractorFake.InvocationType.REFRESH_ALL
            }
        }
    }

    @Test
    @JsName("fn8")
    fun `Given runCommand is called it executes the given Command`() {
        // Given
        val command: CommandMock<StoreContract.WeatherStore> = kmock(
            templateType = RefreshCommandsContract.Command::class,
            relaxUnitFun = true,
        )

        // When
        WeatherStore(interactor).runCommand(command)

        // Then
        verify {
            command._execute.hasBeenCalled()
        }
    }

    @Test
    @JsName("fn0")
    fun `It fulfils WeatherStore`() {
        WeatherStore(interactor) fulfils StoreContract.WeatherStore::class
    }
}
