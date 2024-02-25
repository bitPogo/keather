/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.api

import io.bitpogo.keather.data.weather.model.remote.Forecast
import io.bitpogo.keather.data.weather.model.remote.History
import io.bitpogo.keather.data.weather.model.remote.RequestLocation
import io.bitpogo.keather.data.weather.repository.WeatherRepositoryContract
import io.bitpogo.keather.http.networking.NetworkingContract
import io.bitpogo.keather.http.networking.receive

internal class WeatherApi(
    private val requestBuilder: NetworkingContract.RequestBuilder
) : WeatherRepositoryContract.Remote {
    // q = Latitude and Longitude
    private suspend inline fun <reified T : Any> fetch(
        location: RequestLocation,
        endpoint: String,
        until: Long,
    ): T {
        return requestBuilder.addParameter(
            mapOf(
                "q" to location.toString(),
                "unixdt" to until,
            )
        ).prepare(
            NetworkingContract.Method.GET,
            listOf("v1", "$endpoint.json"),
        ).receive()
    }

    override suspend fun fetchForecast(
        location: RequestLocation,
        until: Long
    ): Forecast = fetch(location, "forecast", until)

    override suspend fun fetchHistory(
        location: RequestLocation,
        until: Long
    ): History = fetch(location, "history", until)
}
