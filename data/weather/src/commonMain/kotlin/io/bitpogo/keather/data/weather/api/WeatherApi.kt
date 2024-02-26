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

internal class WeatherApi(
    private val requestBuilder: NetworkingContract.RequestBuilder,
) : WeatherRepositoryContract.Api {
    // q = Latitude and Longitude
    private suspend inline fun <reified T : Any> fetch(
        location: RequestPosition,
        endpoint: String,
        until: Long,
    ): Result<T> {
        val request = requestBuilder.addParameter(
            mapOf(
                "q" to location.toString(),
                "dt" to until,
            ),
        ).prepare(
            NetworkingContract.Method.GET,
            listOf("v1", "$endpoint.json"),
        )

        return try {
            Result.success(request.receive())
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun fetchForecast(
        position: RequestPosition,
        until: Long,
    ): Result<Forecast> = fetch(position, "forecast", until)

    override suspend fun fetchHistory(
        position: RequestPosition,
        until: Long,
    ): Result<History> = fetch(position, "history", until)
}
