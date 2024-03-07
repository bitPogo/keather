/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.weather

import tech.antibytes.keather.data.location.model.store.SaveableLocation
import tech.antibytes.keather.data.weather.model.api.Forecast as ApiForecast
import tech.antibytes.keather.data.weather.model.api.History
import tech.antibytes.keather.data.weather.model.api.RequestPosition
import tech.antibytes.keather.data.weather.model.store.SaveableForecast
import tech.antibytes.keather.data.weather.model.store.SaveableRealtimeData
import tech.antibytes.keather.entity.Country
import tech.antibytes.keather.entity.Forecast
import tech.antibytes.keather.entity.HistoricData
import tech.antibytes.keather.entity.Latitude
import tech.antibytes.keather.entity.Longitude
import tech.antibytes.keather.entity.Name
import tech.antibytes.keather.entity.Position
import tech.antibytes.keather.entity.Possibility
import tech.antibytes.keather.entity.PrecipitationInMillimeter
import tech.antibytes.keather.entity.RealtimeData
import tech.antibytes.keather.entity.Region
import tech.antibytes.keather.entity.ReturnState
import tech.antibytes.keather.entity.TemperatureInCelsius
import tech.antibytes.keather.entity.Timestamp
import tech.antibytes.keather.entity.WindSpeedInKpH
import tech.antibytes.keather.interactor.repository.RepositoryContract
import kotlin.js.JsName
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.verification.verify
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@OptIn(KMockExperimental::class)
@KMock(
    Clock::class,
    WeatherRepositoryContract.Api::class,
    WeatherRepositoryContract.Store::class,
)
class WeatherRepositorySpec {
    private val fixture = kotlinFixture()
    private val api: ApiMock = kmock()
    private val store: StoreMock = kmock(relaxUnitFun = true)
    private val forecast: ApiForecast = Json.decodeFromString<ApiForecast>(resourceLoader.load("/fixtures/2dayForecast.json")).run {
        copy(
            forecast = forecast.copy(
                forecastDays = forecast.forecastDays.subList(0, 2),
            ),
        )
    }
    private val history: History = Json.decodeFromString<History>(resourceLoader.load("/fixtures/2dayHistory.json")).run {
        copy(
            history = history.copy(
                history = history.history.subList(0, 2),
            ),
        )
    }
    private val location = SaveableLocation(
        latitude = Latitude(forecast.location.latitude),
        longitude = Longitude(forecast.location.longitude),
        name = Name(forecast.location.name),
        region = Region(forecast.location.region),
        country = Country(forecast.location.country),
    )
    private val realtime = SaveableRealtimeData(
        timestamp = forecast.currentDay.lastUpdateTimestamp,
        temperatureInCelsius = forecast.currentDay.temperatureInCelsius,
        windSpeedInKilometerPerHour = forecast.currentDay.windSpeedInKilometerPerHour,
        precipitationInMillimeter = forecast.currentDay.precipitationInMillimeter,
    )
    private val forecasts = listOf(
        SaveableForecast(
            timestamp = forecast.forecast.forecastDays[0].timestamp,
            maximumTemperatureInCelsius = forecast.forecast.forecastDays[0].day.maximumTemperatureInCelsius,
            minimumTemperatureInCelsius = forecast.forecast.forecastDays[0].day.minimumTemperatureInCelsius,
            averageTemperatureInCelsius = forecast.forecast.forecastDays[0].day.averageTemperatureInCelsius,
            maximumWindSpeedInKilometerPerHour = forecast.forecast.forecastDays[0].day.maximumWindSpeedInKilometerPerHour,
            precipitationInMillimeter = forecast.forecast.forecastDays[0].day.precipitationInMillimeter,
            rainPossibility = forecast.forecast.forecastDays[0].day.rainPossibility.toLong(),
        ),
        SaveableForecast(
            timestamp = forecast.forecast.forecastDays[1].timestamp,
            maximumTemperatureInCelsius = forecast.forecast.forecastDays[1].day.maximumTemperatureInCelsius,
            minimumTemperatureInCelsius = forecast.forecast.forecastDays[1].day.minimumTemperatureInCelsius,
            averageTemperatureInCelsius = forecast.forecast.forecastDays[1].day.averageTemperatureInCelsius,
            maximumWindSpeedInKilometerPerHour = forecast.forecast.forecastDays[1].day.maximumWindSpeedInKilometerPerHour,
            precipitationInMillimeter = forecast.forecast.forecastDays[1].day.precipitationInMillimeter,
            rainPossibility = forecast.forecast.forecastDays[1].day.rainPossibility.toLong(),
        ),
    )
    private val historicData = listOf(
        SaveableForecast(
            timestamp = history.history.history[0].timestamp,
            maximumTemperatureInCelsius = history.history.history[0].day.maximumTemperatureInCelsius,
            minimumTemperatureInCelsius = history.history.history[0].day.minimumTemperatureInCelsius,
            averageTemperatureInCelsius = history.history.history[0].day.averageTemperatureInCelsius,
            maximumWindSpeedInKilometerPerHour = history.history.history[0].day.maximumWindSpeedInKilometerPerHour,
            precipitationInMillimeter = history.history.history[0].day.precipitationInMillimeter,
            rainPossibility = history.history.history[0].day.rainPossibility.toLong(),
        ),
        SaveableForecast(
            timestamp = history.history.history[1].timestamp,
            maximumTemperatureInCelsius = history.history.history[1].day.maximumTemperatureInCelsius,
            minimumTemperatureInCelsius = history.history.history[1].day.minimumTemperatureInCelsius,
            averageTemperatureInCelsius = history.history.history[1].day.averageTemperatureInCelsius,
            maximumWindSpeedInKilometerPerHour = history.history.history[1].day.maximumWindSpeedInKilometerPerHour,
            precipitationInMillimeter = history.history.history[1].day.precipitationInMillimeter,
            rainPossibility = history.history.history[1].day.rainPossibility.toLong(),
        ),
    )

    @BeforeTest
    fun setup() {
        api._clearMock()
        store._clearMock()
    }

    @Test
    @JsName("fn1")
    fun `Given getLastUpdateTime is called it returns a failure if no Time had been stored previously`() = runTest {
        // Given
        store._fetchRealtimeData returns Result.failure(IllegalStateException())

        // When
        val timestamp = WeatherRepository(
            StandardTestDispatcher(testScheduler),
            api,
            store,
        ).getLastUpdateTime(this).await()

        // Then
        timestamp.exceptionOrNull() fulfils IllegalStateException::class
    }

    @Test
    @JsName("fn2")
    fun `Given getLastUpdateTime is called it returns the Time of the latest RealtimeData`() = runTest {
        // Given
        val data = SaveableRealtimeData(
            timestamp = fixture.fixture(),
            temperatureInCelsius = fixture.fixture(),
            precipitationInMillimeter = fixture.fixture(),
            windSpeedInKilometerPerHour = fixture.fixture(),
        )
        store._fetchRealtimeData returns Result.success(data)

        // When
        val timestamp = WeatherRepository(
            StandardTestDispatcher(testScheduler),
            api,
            store,
        ).getLastUpdateTime(this).await()

        // Then
        timestamp.getOrNull()?.timestamp mustBe data.timestamp
    }

    @Test
    @JsName("fn3")
    fun `Given updateWeatherData is called it propagates api failures of forecasts directly`() = runTest {
        // Given
        val error = Error()
        val position = Position(
            latitude = Latitude(fixture.fixture()),
            longitude = Longitude(fixture.fixture()),
        )
        api._fetchHistory returns Result.success(history)
        api._fetchForecast returns Result.failure(error)

        // When
        val data = WeatherRepository(
            StandardTestDispatcher(testScheduler),
            api,
            store,
        ).updateWeatherData(position, this).await()

        // Then
        data.exceptionOrNull()!!::class mustBe error::class
    }

    @Test
    @JsName("fn4")
    fun `Given updateWeatherData is called it propagates api failures of history directly`() = runTest {
        // Given
        val error = Error()
        val position = Position(
            latitude = Latitude(fixture.fixture()),
            longitude = Longitude(fixture.fixture()),
        )
        api._fetchForecast returns Result.success(forecast)
        api._fetchHistory returns Result.failure(error)
        store._setLocation returns ReturnState.Success

        // When
        val data = WeatherRepository(
            StandardTestDispatcher(testScheduler),
            api,
            store,
        ).updateWeatherData(position, this).await()

        // Then
        data.exceptionOrNull()!!::class mustBe error::class
    }

    @Test
    @JsName("fn5")
    fun `Given updateWeatherData is called it propagates location storage errors`() = runTest {
        // Given
        val position = Position(
            latitude = Latitude(fixture.fixture()),
            longitude = Longitude(fixture.fixture()),
        )
        api._fetchForecast returns Result.success(forecast)
        api._fetchHistory returns Result.success(history)
        store._setLocation returns ReturnState.Failure

        // When
        val data = WeatherRepository(
            StandardTestDispatcher(testScheduler),
            api,
            store,
        ).updateWeatherData(position, this).await()

        // Then
        data.exceptionOrNull()!!::class mustBe IllegalStateException::class

        verify(exactly = 0) {
            store._setRealtimeData.hasBeenStrictlyCalledWith(realtime)
            store._setForecasts.hasBeenStrictlyCalledWith(forecasts)
        }
    }

    @Test
    @JsName("fn6")
    fun `Given updateWeatherData is called succeeds if the api calls succeed and safes the content`() = runTest {
        // Given
        val position = Position(
            latitude = Latitude(fixture.fixture()),
            longitude = Longitude(fixture.fixture()),
        )

        api._fetchForecast returns Result.success(forecast)
        api._fetchHistory returns Result.success(history)
        store._setLocation returns ReturnState.Success

        // When
        val data = WeatherRepository(
            StandardTestDispatcher(testScheduler),
            api,
            store,
        ).updateWeatherData(position, this).await()

        // Then
        data.getOrNull() mustBe ReturnState.Success

        verify {
            api._fetchForecast.hasBeenStrictlyCalledWith(
                RequestPosition(position.longitude, position.latitude),
            )
            api._fetchHistory.hasBeenStrictlyCalledWith(
                RequestPosition(position.longitude, position.latitude),
            )
            store._setLocation.hasBeenStrictlyCalledWith(location)
            store._setRealtimeData.hasBeenStrictlyCalledWith(realtime)
            store._setForecasts.hasBeenStrictlyCalledWith(forecasts)
            store._setHistoricData.hasBeenStrictlyCalledWith(historicData)
        }
    }

    @Test
    @JsName("fn7")
    fun `Given fetchRealtimeData is called it propagates errors`() = runTest {
        // Given
        val error = Error()
        store._fetchRealtimeData returns Result.failure(error)

        // When
        val data = WeatherRepository(
            StandardTestDispatcher(testScheduler),
            api,
            store,
        ).fetchRealtimeData(this).await()

        // Then
        data.exceptionOrNull()!!::class mustBe error::class
    }

    @Test
    @JsName("fn8")
    fun `Given fetchRealtimeData is called it propagates stored data`() = runTest {
        // Given
        store._fetchRealtimeData returns Result.success(realtime)

        // When
        val data = WeatherRepository(
            StandardTestDispatcher(testScheduler),
            api,
            store,
        ).fetchRealtimeData(this).await()

        // Then
        data.getOrNull() mustBe RealtimeData(
            temperatureInCelsius = TemperatureInCelsius(realtime.temperatureInCelsius),
            windSpeedInKilometerPerHour = WindSpeedInKpH(realtime.windSpeedInKilometerPerHour),
            precipitationInMillimeter = PrecipitationInMillimeter(realtime.precipitationInMillimeter),
        )
    }

    @Test
    @JsName("fn9")
    fun `Given fetchForecast is called it fails if the there are no forecasts`() = runTest {
        // Given
        store._fetchForecasts returns emptyList()

        // When
        val data = WeatherRepository(
            StandardTestDispatcher(testScheduler),
            api,
            store,
        ).fetchForecast(this).await()

        // Then
        data.exceptionOrNull()!!::class mustBe IllegalStateException::class
    }

    @Test
    @JsName("fn10")
    fun `Given fetchForecast is called it propagates stored data`() = runTest {
        // Given
        store._fetchForecasts returns forecasts.subList(0, 1)

        // When
        val data = WeatherRepository(
            StandardTestDispatcher(testScheduler),
            api,
            store,
        ).fetchForecast(this).await()

        // Then
        data.getOrNull() mustBe listOf(
            Forecast(
                timestamp = Timestamp(forecasts.first().timestamp),
                maximumTemperatureInCelsius = TemperatureInCelsius(forecasts.first().maximumTemperatureInCelsius),
                minimumTemperatureInCelsius = TemperatureInCelsius(forecasts.first().minimumTemperatureInCelsius),
                averageTemperatureInCelsius = TemperatureInCelsius(forecasts.first().averageTemperatureInCelsius),
                maximumWindSpeedInKilometerPerHour = WindSpeedInKpH(forecasts.first().maximumWindSpeedInKilometerPerHour),
                precipitationInMillimeter = PrecipitationInMillimeter(forecasts.first().precipitationInMillimeter),
                rainPossibility = Possibility(forecasts.first().rainPossibility),
            ),
        )
    }

    @Test
    @JsName("fn11")
    fun `Given fetchHistoricData is called it fails if the there are no HistoricData`() = runTest {
        // Given
        store._fetchHistoricData returns emptyList()

        // When
        val data = WeatherRepository(
            StandardTestDispatcher(testScheduler),
            api,
            store,
        ).fetchHistoricData(this).await()

        // Then
        data.exceptionOrNull()!!::class mustBe IllegalStateException::class
    }

    @Test
    @JsName("fn12")
    fun `Given fetchHistoricData is called it propagates stored data`() = runTest {
        // Given
        store._fetchHistoricData returns historicData.subList(0, 1)

        // When
        val data = WeatherRepository(
            StandardTestDispatcher(testScheduler),
            api,
            store,
        ).fetchHistoricData(this).await()

        // Then
        data.getOrNull() mustBe listOf(
            HistoricData(
                timestamp = Timestamp(historicData.first().timestamp),
                averageTemperatureInCelsius = TemperatureInCelsius(historicData.first().averageTemperatureInCelsius),
                maximumWindSpeedInKilometerPerHour = WindSpeedInKpH(historicData.first().maximumWindSpeedInKilometerPerHour),
                precipitationInMillimeter = PrecipitationInMillimeter(historicData.first().precipitationInMillimeter),
            ),
        )
    }

    @Test
    @JsName("fn0")
    fun `It fulfils WeatherRepository`() {
        WeatherRepository(
            StandardTestDispatcher(),
            api,
            store,
        ) fulfils RepositoryContract.WeatherRepository::class
    }
}
