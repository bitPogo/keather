/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.repository

import io.bitpogo.keather.data.weather.model.Forecast
import io.bitpogo.keather.data.weather.model.History
import io.bitpogo.keather.data.weather.model.RequestLocation
import io.bitpogo.keather.http.networking.NetworkingContract

internal interface WeatherRepositoryContract {
    fun interface ClientProvider {
        fun provide(): NetworkingContract.RequestBuilder
    }

    interface Remote {
        suspend fun fetchForecast(location: RequestLocation, until: Long): Forecast
        suspend fun fetchHistory(location: RequestLocation, until: Long): History
    }
}
