/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.database

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import io.bitpogo.keather.data.location.LocationQueries
import io.bitpogo.keather.data.location.model.store.SaveableLocation
import io.bitpogo.keather.data.weather.WeatherRepositoryContract
import io.bitpogo.keather.data.weather.model.store.SaveableForecast
import io.bitpogo.keather.data.weather.model.store.SaveableRealtimeData
import io.bitpogo.keather.entity.ReturnState

internal class WeatherStore(
    private val weatherQueries: WeatherQueries,
    private val locationQueries: LocationQueries,
) : WeatherRepositoryContract.Store {
    override suspend fun setLocation(location: SaveableLocation): ReturnState {
        return try {
            locationQueries.set(
                longitude = location.longitude.long,
                latitude = location.latitude.lat,
                name = location.name.name,
                region = location.region.region,
                country = location.country.country,
            )

            ReturnState.Success
        } catch (_: Throwable) {
            ReturnState.Failure
        }
    }

    private fun map(forecast: Forecast): SaveableForecast {
        return SaveableForecast(
            timestamp = forecast.timestamp,
            maximumTemperatureInCelsius = forecast.maximumTemperatureInCelsius,
            minimumTemperatureInCelsius = forecast.minimumTemperatureInCelsius,
            averageTemperatureInCelsius = forecast.averageTemperatureInCelsius,
            maximumWindSpeedInKilometerPerHour = forecast.maximumWindSpeedInKilometerPerHour,
            precipitationInMillimeter = forecast.precipitationInMillimeter,
            rainPossibility = forecast.rainPossibility,
        )
    }

    override suspend fun fetchForecasts(): List<SaveableForecast> {
        return weatherQueries.fetchForecasts().awaitAsList().map(this::map)
    }

    override suspend fun setForecasts(forecasts: List<SaveableForecast>) {
        forecasts.forEach { forecast ->
            weatherQueries.addForecast(
                timestamp = forecast.timestamp,
                maximumTemperatureInCelsius = forecast.maximumTemperatureInCelsius,
                minimumTemperatureInCelsius = forecast.minimumTemperatureInCelsius,
                averageTemperatureInCelsius = forecast.averageTemperatureInCelsius,
                maximumWindSpeedInKilometerPerHour = forecast.maximumWindSpeedInKilometerPerHour,
                precipitationInMillimeter = forecast.precipitationInMillimeter,
                rainPossibility = forecast.rainPossibility,
            )
        }
    }

    private fun map(forecast: History): SaveableForecast {
        return SaveableForecast(
            timestamp = forecast.timestamp,
            maximumTemperatureInCelsius = forecast.maximumTemperatureInCelsius,
            minimumTemperatureInCelsius = forecast.minimumTemperatureInCelsius,
            averageTemperatureInCelsius = forecast.averageTemperatureInCelsius,
            maximumWindSpeedInKilometerPerHour = forecast.maximumWindSpeedInKilometerPerHour,
            precipitationInMillimeter = forecast.precipitationInMillimeter,
            rainPossibility = forecast.rainPossibility,
        )
    }

    override suspend fun fetchHistoricData(): List<SaveableForecast> {
        return weatherQueries.fetchHistoricData().awaitAsList().map(this::map)
    }

    override suspend fun setHistoricData(historicData: List<SaveableForecast>) {
        historicData.forEach { forecast ->
            weatherQueries.addHistoricData(
                timestamp = forecast.timestamp,
                maximumTemperatureInCelsius = forecast.maximumTemperatureInCelsius,
                minimumTemperatureInCelsius = forecast.minimumTemperatureInCelsius,
                averageTemperatureInCelsius = forecast.averageTemperatureInCelsius,
                maximumWindSpeedInKilometerPerHour = forecast.maximumWindSpeedInKilometerPerHour,
                precipitationInMillimeter = forecast.precipitationInMillimeter,
                rainPossibility = forecast.rainPossibility,
            )
        }
    }

    private fun Realtime.map(): SaveableRealtimeData {
        return SaveableRealtimeData(
            timestamp = timestamp,
            temperatureInCelsius = temperatureInCelsius,
            windSpeedInKilometerPerHour = windSpeedInKilometerPerHour,
            precipitationInMillimeter = precipitationInMillimeter,
        )
    }

    override suspend fun fetchRealtimeData(): Result<SaveableRealtimeData> {
        val data = weatherQueries.fetchRealtimeData().awaitAsOneOrNull()

        return if (data == null) {
            Result.failure(IllegalStateException())
        } else {
            Result.success(data.map())
        }
    }

    override suspend fun setRealtimeData(data: SaveableRealtimeData) {
        weatherQueries.setRealtimeData(
            timestamp = data.timestamp,
            temperatureInCelsius = data.temperatureInCelsius,
            windSpeedInKilometerPerHour = data.windSpeedInKilometerPerHour,
            precipitationInMillimeter = data.precipitationInMillimeter,
        )
    }
}
