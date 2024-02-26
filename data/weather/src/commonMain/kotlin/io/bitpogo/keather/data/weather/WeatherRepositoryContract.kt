/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather

import io.bitpogo.keather.data.location.model.store.SaveableLocation
import io.bitpogo.keather.data.weather.model.api.Forecast
import io.bitpogo.keather.data.weather.model.api.History
import io.bitpogo.keather.data.weather.model.api.RequestPosition
import io.bitpogo.keather.data.weather.model.store.SaveableForecast
import io.bitpogo.keather.data.weather.model.store.SaveableRealtimeData
import io.bitpogo.keather.entity.ReturnState
import io.bitpogo.keather.http.networking.NetworkingContract

internal interface WeatherRepositoryContract {
    fun interface ClientProvider {
        fun provide(): NetworkingContract.RequestBuilder
    }

    interface Api {
        suspend fun fetchForecast(position: RequestPosition, until: Long): Result<Forecast>
        suspend fun fetchHistory(position: RequestPosition, until: Long): Result<History>
    }

    // Note: we cloud make even more convenient by couple Forecasts/History with positions and make a new Trigger
    interface Store {
        suspend fun setLocation(location: SaveableLocation): ReturnState
        suspend fun fetchForecasts(): List<SaveableForecast>
        suspend fun setForecasts(forecasts: List<SaveableForecast>)
        suspend fun fetchHistoricData(): List<SaveableForecast>
        suspend fun setHistoricData(historicData: List<SaveableForecast>)
        suspend fun fetchRealtimeData(): Result<SaveableRealtimeData>
        suspend fun setRealtimeData(data: SaveableRealtimeData)
    }
}
