/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.api

import io.bitpogo.keather.data.weather.WeatherRepositoryContract
import io.bitpogo.keather.data.weather.model.api.Forecast
import io.bitpogo.keather.data.weather.model.api.History
import io.bitpogo.keather.data.weather.model.api.RequestPosition
import io.bitpogo.keather.http.networking.NetworkingContract
import io.bitpogo.keather.http.networking.receive
import kotlinx.datetime.Clock

internal class WeatherApi(
    private val clock: Clock,
    private val requestBuilder: NetworkingContract.RequestBuilder,
) : WeatherRepositoryContract.Api {
    // q = Latitude and Longitude
    override suspend fun fetchForecast(
        position: RequestPosition,
    ): Result<Forecast> {
        val request = requestBuilder.addParameter(
            mapOf(
                "q" to position.toString(),
                "unixdt" to clock.now().epochSeconds,
                "days" to FORECAST_IN_DAYS,
            ),
        ).prepare(
            NetworkingContract.Method.GET,
            listOf("v1", "forecast.json"),
        )

        return try {
            Result.success(request.receive())
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun fetchHistory(
        position: RequestPosition,
    ): Result<History> {
        val now = clock.now().epochSeconds
        val request = requestBuilder.addParameter(
            mapOf(
                "q" to position.toString(),
                "unixdt" to now - HISTORY_IN_SECONDS,
                "unixend_dt" to now,
            ),
        ).prepare(
            NetworkingContract.Method.GET,
            listOf("v1", "history.json"),
        )

        return try {
            Result.success(request.receive())
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    private companion object {
        const val FORECAST_IN_DAYS = 7
        const val HISTORY_IN_SECONDS = 1209600
    }
}
