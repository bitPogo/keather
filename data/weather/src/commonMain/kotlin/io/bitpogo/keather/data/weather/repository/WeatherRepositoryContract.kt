/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.repository

import io.bitpogo.keather.data.weather.model.api.Forecast
import io.bitpogo.keather.data.weather.model.api.History
import io.bitpogo.keather.data.weather.model.api.RequestPosition
import io.bitpogo.keather.data.weather.model.store.SaveableForecast
import io.bitpogo.keather.data.weather.model.store.SaveableLocation
import io.bitpogo.keather.http.networking.NetworkingContract

internal interface WeatherRepositoryContract {
    fun interface ClientProvider {
        fun provide(): NetworkingContract.RequestBuilder
    }

    interface Api {
        suspend fun fetchForecast(location: RequestPosition, until: Long): Forecast
        suspend fun fetchHistory(location: RequestPosition, until: Long): History
    }

    interface Store {
        suspend fun setLocation(location: SaveableLocation)
        suspend fun fetchForecast(): List<SaveableForecast>
        suspend fun setForecast(days: List<SaveableForecast>)
        suspend fun fetchHistory(): List<SaveableForecast>
        suspend fun setHistory(days: List<SaveableForecast>)
    }
}
