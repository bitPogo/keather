/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.integration

import io.bitpogo.keather.data.weather.api.ClientProvider
import io.bitpogo.keather.data.weather.api.WeatherApi
import io.bitpogo.keather.data.weather.model.remote.Forecast
import io.bitpogo.keather.data.weather.model.remote.History
import io.bitpogo.keather.data.weather.model.remote.RequestLocation
import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.Longitude
import kotlin.js.JsName
import kotlin.test.Test
import kotlinx.coroutines.GlobalScope
import kotlinx.datetime.Clock
import tech.antibytes.util.test.coroutine.runBlockingTestInContext
import tech.antibytes.util.test.fulfils

// Note this is only a convenience solution...it should actually live in a separate folder
// so it can be run separately
class ClientSpec {
    @Test
    @JsName("fn0")
    fun `Given Client it resolves a Forecast`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        val future = Clock.System.now().epochSeconds + DAY
        val forecast = WeatherApi(ClientProvider().provide()).fetchForecast(
            RequestLocation(Longitude(13.3777), Latitude(52.5162)),
            future,
        )

        forecast fulfils Forecast::class
    }

    @Test
    @JsName("fn1")
    fun `Given Client it resolves History`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        val future = Clock.System.now().epochSeconds - DAY
        val forecast = WeatherApi(ClientProvider().provide()).fetchHistory(
            RequestLocation(Longitude(13.3777), Latitude(52.5162)),
            future,
        )

        forecast fulfils History::class
    }

    companion object {
        const val DAY = 86400
    }
}
