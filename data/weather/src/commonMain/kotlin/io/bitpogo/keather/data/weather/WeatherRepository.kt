/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather

import io.bitpogo.keather.data.location.model.store.SaveableLocation
import io.bitpogo.keather.data.weather.model.api.Forecast as ApiForecast // <-- I should remodel this
import io.bitpogo.keather.data.weather.model.api.History as ApiHistory // <-- I should remodel this
import io.bitpogo.keather.data.weather.model.api.Location as ApiLocation
import io.bitpogo.keather.data.weather.model.api.RequestPosition
import io.bitpogo.keather.data.weather.model.store.SaveableForecast
import io.bitpogo.keather.data.weather.model.store.SaveableRealtimeData
import io.bitpogo.keather.entity.Country
import io.bitpogo.keather.entity.Forecast
import io.bitpogo.keather.entity.HistoricData
import io.bitpogo.keather.entity.Latitude
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
import io.bitpogo.keather.entity.WindSpeedInKpH
import io.bitpogo.keather.interactor.repository.RepositoryContract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

internal class WeatherRepository(
    private val dispatcher: CoroutineDispatcher,
    private val api: WeatherRepositoryContract.Api,
    private val store: WeatherRepositoryContract.Store,
) : RepositoryContract.WeatherRepository {
    private fun <T> defer(
        scope: CoroutineScope,
        context: CoroutineContext = EmptyCoroutineContext,
        action: suspend CoroutineScope.() -> T,
    ): Deferred<T> = scope.async(context = context, block = action)

    private suspend inline fun <T, R> Result<T>.mapSuspendableSuccess(
        crossinline transform: suspend T.() -> R,
    ): Result<R> = mapCatching { transform(it) }

    override fun getLastUpdateTime(
        scope: CoroutineScope,
    ): Deferred<Result<Timestamp>> = defer(scope, dispatcher) {
        store.fetchRealtimeData().map { data -> Timestamp(data.timestamp) }
    }

    private fun Position.toRequestPosition(): RequestPosition = RequestPosition(longitude, latitude)

    private fun ApiLocation.toSaveableLocation(): SaveableLocation {
        return SaveableLocation(
            latitude = Latitude(latitude),
            longitude = Longitude(longitude),
            name = Name(name),
            region = Region(region),
            country = Country(country),
        )
    }

    private fun ApiForecast.toSaveableRealtimeData(): SaveableRealtimeData {
        return SaveableRealtimeData(
            timestamp = currentDay.lastUpdateTimestamp,
            temperatureInCelsius = currentDay.temperatureInCelsius,
            windSpeedInKilometerPerHour = currentDay.windSpeedInKilometerPerHour,
            precipitationInMillimeter = currentDay.precipitationInMillimeter,
        )
    }
    private fun ApiForecast.toSaveableForecast(): List<SaveableForecast> {
        return forecast.forecastDays.map { forecastDay ->
            SaveableForecast(
                timestamp = forecastDay.timestamp,
                maximumTemperatureInCelsius = forecastDay.day.maximumTemperatureInCelsius,
                minimumTemperatureInCelsius = forecastDay.day.minimumTemperatureInCelsius,
                averageTemperatureInCelsius = forecastDay.day.averageTemperatureInCelsius,
                maximumWindSpeedInKilometerPerHour = forecastDay.day.maximumWindSpeedInKilometerPerHour,
                precipitationInMillimeter = forecastDay.day.precipitationInMillimeter,
                rainPossibility = forecastDay.day.rainPossibility.toLong(),
            )
        }
    }

    private suspend fun save(forecast: ApiForecast): ReturnState.Success {
        val stored = store.setLocation(forecast.location.toSaveableLocation())
        if (stored == ReturnState.Failure) { // This is ugly...but time constraints
            throw IllegalStateException()
        } else {
            store.setRealtimeData(forecast.toSaveableRealtimeData())
            store.setForecasts(forecast.toSaveableForecast())
        }

        return ReturnState.Success
    }

    private fun ApiHistory.toSaveableHistoricData(): List<SaveableForecast> {
        return history.history.map { forecastDay ->
            SaveableForecast(
                timestamp = forecastDay.timestamp,
                maximumTemperatureInCelsius = forecastDay.day.maximumTemperatureInCelsius,
                minimumTemperatureInCelsius = forecastDay.day.minimumTemperatureInCelsius,
                averageTemperatureInCelsius = forecastDay.day.averageTemperatureInCelsius,
                maximumWindSpeedInKilometerPerHour = forecastDay.day.maximumWindSpeedInKilometerPerHour,
                precipitationInMillimeter = forecastDay.day.precipitationInMillimeter,
                rainPossibility = forecastDay.day.rainPossibility.toLong(),
            )
        }
    }

    private suspend fun save(history: ApiHistory): ReturnState.Success {
        store.setHistoricData(history.toSaveableHistoricData())

        return ReturnState.Success
    }

    override fun updateWeatherData(
        position: Position,
        scope: CoroutineScope,
    ): Deferred<Result<ReturnState.Success>> = defer(scope, dispatcher) {
        val requestPosition = position.toRequestPosition()

        val forecasts = async {
            api.fetchForecast(requestPosition).mapSuspendableSuccess(::save)
        }
        val history = async {
            api.fetchHistory(requestPosition).mapSuspendableSuccess(::save)
        }

        awaitAll(forecasts, history).map {
            if (it.isSuccess) {
                Result.success(ReturnState.Success)
            } else {
                Result.failure(it.exceptionOrNull()!!)
            }
        }.firstOrNull { it.isFailure } ?: Result.success(ReturnState.Success)
    }

    private fun map(saveableRealtimeData: SaveableRealtimeData): RealtimeData {
        return RealtimeData(
            temperatureInCelsius = TemperatureInCelsius(saveableRealtimeData.temperatureInCelsius),
            windSpeedInKilometerPerHour = WindSpeedInKpH(saveableRealtimeData.windSpeedInKilometerPerHour),
            precipitationInMillimeter = PrecipitationInMillimeter(saveableRealtimeData.precipitationInMillimeter),
        )
    }

    override fun fetchRealtimeData(
        scope: CoroutineScope,
    ): Deferred<Result<RealtimeData>> = defer(scope, dispatcher) {
        store.fetchRealtimeData().map(::map)
    }

    private fun mapForecast(saveableForecast: SaveableForecast): Forecast {
        return Forecast(
            timestamp = Timestamp(saveableForecast.timestamp),
            maximumTemperatureInCelsius = TemperatureInCelsius(saveableForecast.maximumTemperatureInCelsius),
            minimumTemperatureInCelsius = TemperatureInCelsius(saveableForecast.minimumTemperatureInCelsius),
            averageTemperatureInCelsius = TemperatureInCelsius(saveableForecast.averageTemperatureInCelsius),
            maximumWindSpeedInKilometerPerHour = WindSpeedInKpH(saveableForecast.maximumWindSpeedInKilometerPerHour),
            precipitationInMillimeter = PrecipitationInMillimeter(saveableForecast.precipitationInMillimeter),
            rainPossibility = Possibility(saveableForecast.rainPossibility),
        )
    }

    override fun fetchForecast(
        scope: CoroutineScope,
    ): Deferred<Result<List<Forecast>>> = defer(scope, dispatcher) {
        val forecasts = store.fetchForecasts()

        if (forecasts.isEmpty()) {
            Result.failure(IllegalStateException())
        } else {
            Result.success(forecasts.map(::mapForecast))
        }
    }

    private fun mapHistoricData(saveableForecast: SaveableForecast): HistoricData {
        return HistoricData(
            timestamp = Timestamp(saveableForecast.timestamp),
            averageTemperatureInCelsius = TemperatureInCelsius(saveableForecast.averageTemperatureInCelsius),
            maximumWindSpeedInKilometerPerHour = WindSpeedInKpH(saveableForecast.maximumWindSpeedInKilometerPerHour),
            precipitationInMillimeter = PrecipitationInMillimeter(saveableForecast.precipitationInMillimeter),
        )
    }

    override fun fetchHistoricData(
        scope: CoroutineScope,
    ): Deferred<Result<List<HistoricData>>> = defer(scope, dispatcher) {
        val forecasts = store.fetchHistoricData()

        if (forecasts.isEmpty()) {
            Result.failure(IllegalStateException())
        } else {
            Result.success(forecasts.map(::mapHistoricData))
        }
    }
}
