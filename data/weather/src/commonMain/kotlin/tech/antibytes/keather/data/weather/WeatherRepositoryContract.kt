/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.weather

import tech.antibytes.keather.data.location.model.store.SaveableLocation
import tech.antibytes.keather.data.weather.model.api.Forecast
import tech.antibytes.keather.data.weather.model.api.History
import tech.antibytes.keather.data.weather.model.api.RequestPosition
import tech.antibytes.keather.data.weather.model.store.SaveableForecast
import tech.antibytes.keather.data.weather.model.store.SaveableRealtimeData
import tech.antibytes.keather.entity.ReturnState
import tech.antibytes.keather.http.networking.NetworkingContract

internal interface WeatherRepositoryContract {
    fun interface ClientProvider {
        fun provide(): NetworkingContract.RequestBuilder
    }

    interface Api {
        suspend fun fetchForecast(position: RequestPosition): Result<Forecast>
        suspend fun fetchHistory(position: RequestPosition): Result<History>
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
