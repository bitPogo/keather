/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.interactor

import app.cash.turbine.test
import io.bitpogo.keather.entity.Country
import io.bitpogo.keather.entity.Forecast
import io.bitpogo.keather.entity.HistoricData
import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.LocalizedWeatherData
import io.bitpogo.keather.entity.Location
import io.bitpogo.keather.entity.Longitude
import io.bitpogo.keather.entity.Name
import io.bitpogo.keather.entity.Position
import io.bitpogo.keather.entity.Possibility
import io.bitpogo.keather.entity.PrecipitationInMillimeter
import io.bitpogo.keather.entity.RealtimeData
import io.bitpogo.keather.entity.Region
import io.bitpogo.keather.entity.ReturnState
import io.bitpogo.keather.entity.TemperatureInCelsius
import io.bitpogo.keather.entity.Timestamp
import io.bitpogo.keather.entity.WeatherError.PlaceholderError
import io.bitpogo.keather.entity.WindSpeedInKpH
import io.bitpogo.keather.interactor.repository.LocationRepositoryMock
import io.bitpogo.keather.interactor.repository.PositionRepositoryMock
import io.bitpogo.keather.interactor.repository.RepositoryContract
import io.bitpogo.keather.interactor.repository.WeatherRepositoryMock
import io.bitpogo.keather.presentation.interactor.InteractorContract
import kotlin.js.JsName
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.verification.Asserter
import tech.antibytes.kmock.verification.verify
import tech.antibytes.kmock.verification.verifyOrder
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@OptIn(KMockExperimental::class)
@KMock(
    RepositoryContract.WeatherRepository::class,
    RepositoryContract.PositionRepository::class,
    RepositoryContract.LocationRepository::class,
)
class WeatherDataInteractorSpec {
    private val fixture = kotlinFixture()
    private val callCollector = Asserter()
    private val weatherRepository: WeatherRepositoryMock = kmock(callCollector)
    private val positionRepository: PositionRepositoryMock = kmock()
    private val locationRepository: LocationRepositoryMock = kmock(callCollector)
    private val position = Position(
        Latitude(fixture.fixture()),
        Longitude(fixture.fixture()),
    )
    private val realtimeData = RealtimeData(
        temperatureInCelsius = TemperatureInCelsius(fixture.fixture()),
        windSpeedInKilometerPerHour = WindSpeedInKpH(fixture.fixture()),
        precipitationInMillimeter = PrecipitationInMillimeter(fixture.fixture()),
    )
    private val forecast = listOf(
        Forecast(
            timestamp = Timestamp(fixture.fixture()),
            maximumTemperatureInCelsius = TemperatureInCelsius(fixture.fixture()),
            minimumTemperatureInCelsius = TemperatureInCelsius(fixture.fixture()),
            averageTemperatureInCelsius = TemperatureInCelsius(fixture.fixture()),
            maximumWindSpeedInKilometerPerHour = WindSpeedInKpH(fixture.fixture()),
            precipitationInMillimeter = PrecipitationInMillimeter(fixture.fixture()),
            rainPossibility = Possibility(fixture.fixture()),
        ),
    )
    private val history = listOf(
        HistoricData(
            timestamp = Timestamp(fixture.fixture()),
            averageTemperatureInCelsius = TemperatureInCelsius(fixture.fixture()),
            maximumWindSpeedInKilometerPerHour = WindSpeedInKpH(fixture.fixture()),
            precipitationInMillimeter = PrecipitationInMillimeter(fixture.fixture()),
        ),
    )
    private val location = Location(
        name = Name(fixture.fixture()),
        country = Country(fixture.fixture()),
        region = Region(fixture.fixture()),
    )

    @BeforeTest
    fun setUp() {
        weatherRepository._clearMock()
        positionRepository._clearMock()
        locationRepository._clearMock()
    }

    @Test
    @JsName("fn3")
    fun `Given refreshAll is called it propagates PositionErrors`() = runTest {
        // Given
        val error = Result.failure<Position>(Error())
        positionRepository._refreshPosition returns async { error }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refreshAll(this)

            // Then
            awaitItem() mustBe Result.failure(PlaceholderError)
        }
    }

    @Test
    @JsName("fn4")
    fun `Given refreshAll is called it propagates UpdateWeatherDataErrors`() = runTest {
        // Given
        val position = Position(
            Latitude(fixture.fixture()),
            Longitude(fixture.fixture()),
        )
        val error = Result.failure<ReturnState.Success>(Error())

        positionRepository._refreshPosition returns async { Result.success(position) }
        weatherRepository._updateWeatherData returns async { error }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refreshAll(this)

            // Then
            awaitItem() mustBe Result.failure(PlaceholderError)

            verify {
                weatherRepository._updateWeatherData.hasBeenCalledWith(position)
            }
        }
    }

    @Test
    @JsName("fn5")
    fun `Given refreshAll is called it propagates RealtimeDataErrors`() = runTest {
        // Given
        val error = Result.failure<RealtimeData>(Error())

        positionRepository._refreshPosition returns async { Result.success(position) }
        weatherRepository._updateWeatherData returns async { Result.success(ReturnState.Success) }
        weatherRepository._fetchRealtimeData returns async { error }
        weatherRepository._fetchForecast returns async { Result.success(forecast) }
        weatherRepository._fetchHistoricData returns async { Result.success(history) }
        locationRepository._fetchLocation returns async { Result.success(location) }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refreshAll(this)

            // Then
            awaitItem() mustBe Result.failure(PlaceholderError)

            callCollector.verifyOrder {
                weatherRepository._updateWeatherData.hasBeenCalledWith(position)
                weatherRepository._fetchRealtimeData.hasBeenCalled()
            }
        }
    }

    @Test
    @JsName("fn6")
    fun `Given refreshAll is called it propagates ForecastsErrors`() = runTest {
        // Given
        val position = Position(
            Latitude(fixture.fixture()),
            Longitude(fixture.fixture()),
        )
        val realtimeData = RealtimeData(
            temperatureInCelsius = TemperatureInCelsius(fixture.fixture()),
            windSpeedInKilometerPerHour = WindSpeedInKpH(fixture.fixture()),
            precipitationInMillimeter = PrecipitationInMillimeter(fixture.fixture()),
        )
        val error = Result.failure<List<Forecast>>(Error())

        positionRepository._refreshPosition returns async { Result.success(position) }
        weatherRepository._updateWeatherData returns async { Result.success(ReturnState.Success) }
        weatherRepository._fetchRealtimeData returns async { Result.success(realtimeData) }
        weatherRepository._fetchForecast returns async { error }
        weatherRepository._fetchHistoricData returns async { Result.success(history) }
        locationRepository._fetchLocation returns async { Result.success(location) }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refreshAll(this)

            // Then
            awaitItem() mustBe Result.failure(PlaceholderError)

            callCollector.verifyOrder {
                weatherRepository._updateWeatherData.hasBeenCalledWith(position)
                weatherRepository._fetchForecast.hasBeenCalled()
            }
        }
    }

    @Test
    @JsName("fn7")
    fun `Given refreshAll is called it propagates HistoricDataErrors`() = runTest {
        // Given

        val error = Result.failure<List<HistoricData>>(Error())

        positionRepository._refreshPosition returns async { Result.success(position) }
        weatherRepository._updateWeatherData returns async { Result.success(ReturnState.Success) }
        weatherRepository._fetchRealtimeData returns async { Result.success(realtimeData) }
        weatherRepository._fetchForecast returns async { Result.success(forecast) }
        weatherRepository._fetchHistoricData returns async { error }
        locationRepository._fetchLocation returns async { Result.success(location) }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refreshAll(this)

            // Then
            awaitItem() mustBe Result.failure(PlaceholderError)

            callCollector.verifyOrder {
                weatherRepository._updateWeatherData.hasBeenCalledWith(position)
                weatherRepository._fetchHistoricData.hasBeenCalled()
            }
        }
    }

    @Test
    @JsName("fn8")
    fun `Given refreshAll is called it propagates LocationErrors`() = runTest {
        // Given
        val error = Result.failure<Location>(Error())

        positionRepository._refreshPosition returns async { Result.success(position) }
        weatherRepository._updateWeatherData returns async { Result.success(ReturnState.Success) }
        weatherRepository._fetchRealtimeData returns async { Result.success(realtimeData) }
        weatherRepository._fetchForecast returns async { Result.success(forecast) }
        weatherRepository._fetchHistoricData returns async { Result.success(history) }
        locationRepository._fetchLocation returns async { error }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refreshAll(this)

            // Then
            awaitItem() mustBe Result.failure(PlaceholderError)

            callCollector.verifyOrder {
                weatherRepository._updateWeatherData.hasBeenCalledWith(position)
                locationRepository._fetchLocation.hasBeenCalled()
            }
        }
    }

    @Test
    @JsName("fn9")
    fun `Given refreshAll is called it propagates all updated data`() = runTest {
        // Given
        positionRepository._refreshPosition returns async { Result.success(position) }
        weatherRepository._updateWeatherData returns async { Result.success(ReturnState.Success) }
        weatherRepository._fetchRealtimeData returns async { Result.success(realtimeData) }
        weatherRepository._fetchForecast returns async { Result.success(forecast) }
        weatherRepository._fetchHistoricData returns async { Result.success(history) }
        locationRepository._fetchLocation returns async { Result.success(location) }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refreshAll(this)

            // Then
            awaitItem().getOrNull() mustBe LocalizedWeatherData(
                location = location,
                realtimeData = realtimeData,
                history = history,
                forecast = forecast,
            )
        }
    }

    @Test
    @JsName("fn10")
    fun `Given refresh is called it propagates PositionErrors`() = runTest {
        // Given
        val error = Result.failure<Position>(Error())
        positionRepository._fetchPosition returns async { error }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refresh(this)

            // Then
            awaitItem() mustBe Result.failure(PlaceholderError)
        }
    }

    @Test
    @JsName("fn11")
    fun `Given refresh is called it propagates UpdateWeatherDataErrors`() = runTest {
        // Given
        val position = Position(
            Latitude(fixture.fixture()),
            Longitude(fixture.fixture()),
        )
        val error = Result.failure<ReturnState.Success>(Error())

        positionRepository._fetchPosition returns async { Result.success(position) }
        weatherRepository._updateWeatherData returns async { error }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refresh(this)

            // Then
            awaitItem() mustBe Result.failure(PlaceholderError)

            verify {
                weatherRepository._updateWeatherData.hasBeenCalledWith(position)
            }
        }
    }

    @Test
    @JsName("fn12")
    fun `Given refresh is called it propagates RealtimeDataErrors`() = runTest {
        // Given
        val error = Result.failure<RealtimeData>(Error())

        positionRepository._fetchPosition returns async { Result.success(position) }
        weatherRepository._updateWeatherData returns async { Result.success(ReturnState.Success) }
        weatherRepository._fetchRealtimeData returns async { error }
        weatherRepository._fetchForecast returns async { Result.success(forecast) }
        weatherRepository._fetchHistoricData returns async { Result.success(history) }
        locationRepository._fetchLocation returns async { Result.success(location) }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refresh(this)

            // Then
            awaitItem() mustBe Result.failure(PlaceholderError)

            callCollector.verifyOrder {
                weatherRepository._updateWeatherData.hasBeenCalledWith(position)
                weatherRepository._fetchRealtimeData.hasBeenCalled()
            }
        }
    }

    @Test
    @JsName("fn13")
    fun `Given refresh is called it propagates ForecastsErrors`() = runTest {
        // Given
        val position = Position(
            Latitude(fixture.fixture()),
            Longitude(fixture.fixture()),
        )
        val realtimeData = RealtimeData(
            temperatureInCelsius = TemperatureInCelsius(fixture.fixture()),
            windSpeedInKilometerPerHour = WindSpeedInKpH(fixture.fixture()),
            precipitationInMillimeter = PrecipitationInMillimeter(fixture.fixture()),
        )
        val error = Result.failure<List<Forecast>>(Error())

        positionRepository._fetchPosition returns async { Result.success(position) }
        weatherRepository._updateWeatherData returns async { Result.success(ReturnState.Success) }
        weatherRepository._fetchRealtimeData returns async { Result.success(realtimeData) }
        weatherRepository._fetchForecast returns async { error }
        weatherRepository._fetchHistoricData returns async { Result.success(history) }
        locationRepository._fetchLocation returns async { Result.success(location) }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refresh(this)

            // Then
            awaitItem() mustBe Result.failure(PlaceholderError)

            callCollector.verifyOrder {
                weatherRepository._updateWeatherData.hasBeenCalledWith(position)
                weatherRepository._fetchForecast.hasBeenCalled()
            }
        }
    }

    @Test
    @JsName("fn14")
    fun `Given refresh is called it propagates HistoricDataErrors`() = runTest {
        // Given

        val error = Result.failure<List<HistoricData>>(Error())

        positionRepository._fetchPosition returns async { Result.success(position) }
        weatherRepository._updateWeatherData returns async { Result.success(ReturnState.Success) }
        weatherRepository._fetchRealtimeData returns async { Result.success(realtimeData) }
        weatherRepository._fetchForecast returns async { Result.success(forecast) }
        weatherRepository._fetchHistoricData returns async { error }
        locationRepository._fetchLocation returns async { Result.success(location) }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refresh(this)

            // Then
            awaitItem() mustBe Result.failure(PlaceholderError)

            callCollector.verifyOrder {
                weatherRepository._updateWeatherData.hasBeenCalledWith(position)
                weatherRepository._fetchHistoricData.hasBeenCalled()
            }
        }
    }

    @Test
    @JsName("fn15")
    fun `Given refresh is called it propagates LocationErrors`() = runTest {
        // Given
        val error = Result.failure<Location>(Error())

        positionRepository._fetchPosition returns async { Result.success(position) }
        weatherRepository._updateWeatherData returns async { Result.success(ReturnState.Success) }
        weatherRepository._fetchRealtimeData returns async { Result.success(realtimeData) }
        weatherRepository._fetchForecast returns async { Result.success(forecast) }
        weatherRepository._fetchHistoricData returns async { Result.success(history) }
        locationRepository._fetchLocation returns async { error }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refresh(this)

            // Then
            awaitItem() mustBe Result.failure(PlaceholderError)

            callCollector.verifyOrder {
                weatherRepository._updateWeatherData.hasBeenCalledWith(position)
                locationRepository._fetchLocation.hasBeenCalled()
            }
        }
    }

    @Test
    @JsName("fn16")
    fun `Given refresh is called it propagates all updated data`() = runTest {
        // Given
        positionRepository._fetchPosition returns async { Result.success(position) }
        weatherRepository._updateWeatherData returns async { Result.success(ReturnState.Success) }
        weatherRepository._fetchRealtimeData returns async { Result.success(realtimeData) }
        weatherRepository._fetchForecast returns async { Result.success(forecast) }
        weatherRepository._fetchHistoricData returns async { Result.success(history) }
        locationRepository._fetchLocation returns async { Result.success(location) }

        // When
        val interactor = WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        )

        interactor.weatherData.test {
            // When
            interactor.refresh(this)

            // Then
            awaitItem().getOrNull() mustBe LocalizedWeatherData(
                location = location,
                realtimeData = realtimeData,
                history = history,
                forecast = forecast,
            )
        }
    }

    @Test
    @JsName("fn0")
    fun `It fulfils WeatherDataInteractor`() {
        WeatherDataInteractor(
            positionRepository,
            weatherRepository,
            locationRepository,
        ) fulfils InteractorContract.WeatherDataInteractor::class
    }
}
