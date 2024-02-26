/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.interactor.repository

import io.bitpogo.keather.entity.Forecast
import io.bitpogo.keather.entity.HistoricData
import io.bitpogo.keather.entity.Location
import io.bitpogo.keather.entity.Position
import io.bitpogo.keather.entity.RealtimeData
import io.bitpogo.keather.entity.ReturnState
import io.bitpogo.keather.entity.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

interface RepositoryContract {
    interface PositionRepository {
        fun refreshPosition(scope: CoroutineScope): Deferred<Result<Position>>
        fun fetchPosition(scope: CoroutineScope): Deferred<Result<Position>>
    }

    fun interface LocationRepository {
        fun fetchLocation(scope: CoroutineScope): Deferred<Result<Location>>
    }

    interface WeatherRepository {
        fun getLastUpdateTime(scope: CoroutineScope): Deferred<Result<Timestamp>>
        fun fetchRealtimeData(scope: CoroutineScope): Deferred<Result<RealtimeData>>
        fun fetchForecast(scope: CoroutineScope): Deferred<Result<List<Forecast>>>
        fun fetchHistoricData(scope: CoroutineScope): Deferred<Result<List<HistoricData>>>
        fun updateWeatherData(position: Position, scope: CoroutineScope): Deferred<Result<ReturnState.Success>>
    }
}
